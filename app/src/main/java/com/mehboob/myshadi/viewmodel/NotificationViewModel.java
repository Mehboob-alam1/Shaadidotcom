package com.mehboob.myshadi.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.mehboob.myshadi.model.profilemodel.NotificationData;
import com.mehboob.myshadi.repository.NotificationRepository;

import java.util.List;

public class NotificationViewModel extends AndroidViewModel {

    private NotificationRepository notificationRepository;
    private MutableLiveData<List<NotificationData>> notificationsDataList;
    public NotificationViewModel( Application application) {
        super(application);

        notificationRepository= new NotificationRepository(application);
        notificationsDataList=notificationRepository.getNotificationDataList();

    }

    public void fetchNotificationsIRecieved(String userId){

        notificationRepository.fetchNotificationsIRecieved(userId);
    }

    public MutableLiveData<List<NotificationData>> getNotificationsDataList() {
        return notificationsDataList;
    }
}
