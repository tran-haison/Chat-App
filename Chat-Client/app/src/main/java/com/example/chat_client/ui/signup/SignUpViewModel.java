package com.example.chat_client.ui.signup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chat_client.models.User;

public class SignUpViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isInputTextValid = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isUserExisted = new MutableLiveData<>();

    public SignUpViewModel() {}

    public void checkInputText(String username, String password, String passwordRetype) {
        isInputTextValid.setValue(!username.isEmpty()
                && !password.isEmpty()
                && !passwordRetype.isEmpty()
                && password.equals(passwordRetype));
    }

    public LiveData<Boolean> getInputTextState() {
        return isInputTextValid;
    }

    public void signUpUser(User user) {
        // TODO: connect to server to check if user is existed or not
        // if user already exist -> return
        // else sign up user
    }

    public LiveData<Boolean> getUserState() {
        return isUserExisted;
    }
}
