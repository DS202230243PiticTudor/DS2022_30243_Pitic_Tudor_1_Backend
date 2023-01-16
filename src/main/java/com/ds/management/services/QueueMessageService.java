package com.ds.management.services;

import com.ds.management.domain.dtos.QueueMessage;
import com.ds.management.domain.entities.Device;
import com.ds.management.domain.entities.DeviceReadingPair;
import com.ds.management.domain.entities.Notification;
import com.ds.management.domain.repositories.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
public class QueueMessageService {
    private DeviceRepository deviceRepository;
    private PersonService personService;
    private NotificationService notificationService;
    private WebSocketService webSocketService;

    @Autowired
    public QueueMessageService(
            DeviceRepository deviceRepository,
            PersonService personService,
            NotificationService notificationService,
            WebSocketService webSocketService
    ) {
        this.deviceRepository = deviceRepository;
        this.personService = personService;
        this.notificationService = notificationService;
        this.webSocketService = webSocketService;
    }

    public void processQueueMessages(List<QueueMessage> queueMessageList) {
        List<Device> deviceList = new ArrayList<>();
        List<DeviceReadingPair> pairs = new ArrayList<>();
        UUID personId = null;
        Date createdDate = null;
        for(QueueMessage message : queueMessageList) {
            personId = message.getPersonId();
            createdDate = message.getRecordedDate();
            Optional<Device> deviceOptional = this.deviceRepository.findById(message.getDeviceId());
            if(deviceOptional.isEmpty()) {
                throw new EntityNotFoundException(Device.class.getSimpleName() + " with id: " + message.getDeviceId());
            }
            deviceList.add(deviceOptional.get());
            if(message.getEnergyConsumption() > deviceOptional.get().getMaxEnergyConsumption()) {
                // reading exceeds max
                // add notification to notification list
                Notification notification = Notification.builder()
                        .personId(message.getPersonId())
                        .deviceId(message.getDeviceId())
                        .currentReading(message.getEnergyConsumption())
                        .differenceInReading(message.getEnergyConsumption() - deviceOptional.get().getMaxEnergyConsumption())
                        .readingDate(createdDate)
                        .build();
                this.notificationService.addNotification(notification);
                // send notification to frontend client, but doesn't delete notification from db
                this.webSocketService.notifyFrontendUser(personId.toString(), notification);
                // add new deviceReadingPair with max value for current reading
                DeviceReadingPair pair = DeviceReadingPair.builder()
                        .deviceId(deviceOptional.get().getId())
                        .reading(deviceOptional.get().getMaxEnergyConsumption())
                        .build();
                pairs.add(pair);

            } else {
                // make new deviceReadingPair
                DeviceReadingPair pair = DeviceReadingPair.builder()
                        .deviceId(deviceOptional.get().getId())
                        .reading(message.getEnergyConsumption())
                        .build();
                pairs.add(pair);
            }
        }
        if(personId != null && createdDate != null) {
            this.personService.makeMeasurementWithDeviceReadingPairList(personId, pairs, createdDate);
        }
    }
}
