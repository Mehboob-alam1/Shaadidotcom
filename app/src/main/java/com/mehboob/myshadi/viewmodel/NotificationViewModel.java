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
    private MutableLiveData<Boolean> notificationDeleted;
    public NotificationViewModel( Application application) {
        super(application);

        notificationRepository= new NotificationRepository(application);
        notificationsDataList=notificationRepository.getNotificationDataList();
        notificationDeleted=notificationRepository.getNotificationDeleted();

    }

    public void fetchNotificationsIRecieved(String userId){

        notificationRepository.fetchNotificationsIRecieved(userId);
    }


    public void deleteNotification(String myId,String otherUserId){
        notificationRepository.deleteNotification(myId,otherUserId);
    }
    public MutableLiveData<List<NotificationData>> getNotificationsDataList() {
        return notificationsDataList;
    }


    public MutableLiveData<Boolean> getNotificationDeleted() {
        return notificationDeleted;
    }
}
