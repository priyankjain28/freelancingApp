package com.peeru.task.freelancingapp.ui.fragment;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.peeru.task.freelancingapp.R;
import com.peeru.task.freelancingapp.data.model.User;
import com.peeru.task.freelancingapp.databinding.FragmentSignupBinding;
import com.peeru.task.freelancingapp.init.AppController;
import com.peeru.task.freelancingapp.ui.push.SharedPrefManager;
import com.peeru.task.freelancingapp.ui.viewmodel.LoginSignUpViewModel;
import com.peeru.task.freelancingapp.util.Constant;

@SuppressLint("ValidFragment")
public class Signup extends Fragment {
    //region Variable declaration
    FragmentSignupBinding mBinding;
    private LoginSignUpViewModel loginSignUpViewModel;
    //endregion

    //region Fragment Lifecycle component
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup,
                container, false);
        loginSignUpViewModel = ViewModelProviders.of(getActivity()).get(LoginSignUpViewModel.class);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intializeLayoutVariable();
        setButtonClickListner();
    }
    //endregion

    //region Intialize Layout Variable
    private void intializeLayoutVariable() {
        mBinding.firstName.addTextChangedListener(new MyTextWatcher(mBinding.firstName));
        mBinding.lastName.addTextChangedListener(new MyTextWatcher(mBinding.lastName));
        mBinding.email.addTextChangedListener(new MyTextWatcher(mBinding.email));
        mBinding.ipassword.addTextChangedListener(new MyTextWatcher(mBinding.ipassword));
    }

    private void setButtonClickListner() {
        mBinding.signup.setOnClickListener(view -> {
            Log.d("SignUp","Hora Freelancing Submit Form ");
            submitForm();
        });
    }
    //endregion

    //region Validation Form
    private void submitForm() {
        if (!validateName(mBinding.firstnameWrapper)) {
            Log.d("SignUp","Hora Freelancing First Name Validation Failed ");
            return;
        }

        if (!validateName(mBinding.lastnameWrapper)) {
            Log.d("SignUp","Hora Freelancing Last Name Validation Failed ");
            return;
        }


        if (!validateEmail()) {
            Log.d("SignUp","Hora Freelancing Email Validation Failed ");
            return;
        }

        if (!validatePassword()) {
            Log.d("SignUp","Hora Freelancing Password Validation Failed ");

            return;
        }


        if (!validateUserRole()) {
            Log.d("SignUp","Hora Freelancing User Role Validation Failed ");

            return;
        }

        saveAndRegisterFormData();
        // Toast.makeText(this, "Thank You!", Toast.LENGTH_SHORT).show();
    }


    private boolean validateName(TextInputLayout inputLayoutName) {
        if (inputLayoutName.getEditText().getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputLayoutName.getEditText());
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = mBinding.email.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            mBinding.emailWrapper.setError(getString(R.string.err_msg_email));
            requestFocus(mBinding.email);
            return false;
        } else {
            mBinding.emailWrapper.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (mBinding.ipassword.getText().toString().trim().isEmpty()) {
            mBinding.ipasswordWrapper.setError(getString(R.string.err_msg_password));
            requestFocus(mBinding.ipassword);
            return false;
        } else {
            mBinding.ipasswordWrapper.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateUserRole() {
        if (mBinding.userRole.getSelectedItem().toString().equals("Select User Type")) {
            Toast.makeText(getContext(), Constant.USER_ROLE_MANDATORY, Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    //endregion

    //region Save and register user
    private void saveAndRegisterFormData() {
        User user = new User();
        user.setFirstName(mBinding.firstName.getText().toString());
        user.setLastName(mBinding.lastName.getText().toString());
        user.setEmail(mBinding.email.getText().toString());
        user.setPassword(mBinding.ipassword.getText().toString());
        user.setUserRole(mBinding.userRole.getSelectedItem().toString());
        user.setLastUpdate(String.valueOf(System.currentTimeMillis()));
        user.setDateCreated(String.valueOf(System.currentTimeMillis()));
        user.setDeviceId(SharedPrefManager.getInstance(AppController.getAppContext()).getDeviceToken());
        user.setRating(Constant.USER_DEFAULT_RATING);
        user.setUserStatus(Constant.USER_DEFAULT_STATUS);
        Log.d("SignUp","Hora Freelancing Signup: "+user.toString());
        loginSignUpViewModel.creatNewUser(user, getActivity());
    }
    //endregion

    //region TextWatcher Class to watch text
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.first_name:
                    validateName(mBinding.firstnameWrapper);
                    break;
                case R.id.last_name:
                    validateName(mBinding.lastnameWrapper);
                    break;
                case R.id.email:
                    validateEmail();
                    break;
                case R.id.ipassword:
                    validatePassword();
                    break;
            }
        }
    }
    //endregion
}