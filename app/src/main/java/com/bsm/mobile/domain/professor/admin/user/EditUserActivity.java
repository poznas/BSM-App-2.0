package com.bsm.mobile.domain.professor.admin.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.bsm.mobile.R;
import com.bsm.mobile.legacy.model.User;
import com.bsm.mobile.root.App;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.bsm.mobile.common.resource.Constants.KEY_USER;
import static com.bsm.mobile.common.utils.UserDataValidator.VALID_GENDERS;
import static com.bsm.mobile.common.utils.UserDataValidator.VALID_LABELS;
import static com.bsm.mobile.common.utils.UserDataValidator.VALID_TEAMS;
import static com.bsm.mobile.domain.professor.admin.user.EditUserActivityMVP.Presenter;
import static com.bsm.mobile.domain.professor.admin.user.EditUserActivityMVP.View;

public class EditUserActivity extends AppCompatActivity implements View {

    public static final String SPINNER_NONE_ITEM = "null            ";

    @Inject
    Presenter presenter;

    @BindView(R.id.user_image_view)
    CircleImageView userImageView;
    @BindView(R.id.user_email_view)
    TextView emailTextView;
    @BindView(R.id.display_name_edit_view)
    EditText displayNameEditText;
    @BindView(R.id.label_spinner)
    Spinner labelSpinner;
    @BindView(R.id.team_spinner)
    Spinner teamSpinner;
    @BindView(R.id.gender_spinner)
    Spinner genderSpinner;
    @BindView(R.id.fb_url_edit_view)
    TextView facebookUrlEditText;

    @BindView(R.id.make_judge_button)
    android.view.View makeJudgeButton;
    @BindView(R.id.update_button)
    android.view.View updateButton;

    ArrayAdapter<String> labelAdapter;
    ArrayAdapter<String> teamAdapter;
    ArrayAdapter<String> genderAdapter;

    private User user;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_edit_user);
        ButterKnife.bind(this);

        ((App) getApplication()).getComponent().inject(this);
        presenter.attachView(this);

        initializeSpinners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.handleBundle((User) getIntent().getSerializableExtra(KEY_USER));
    }

    @Override
    public void displayUserData(User user) {
        this.user = user;

        Glide.with(this).load(user.getPhotoUrl()).into(userImageView);
        emailTextView.setText(user.getEmail());
        displayNameEditText.setText(user.getDisplayName());
        setSpinnerItem(labelSpinner, labelAdapter, user.getLabel());
        setSpinnerItem(teamSpinner, teamAdapter, user.getTeam());
        setSpinnerItem(genderSpinner, genderAdapter, user.getGender());
        facebookUrlEditText.setText(user.getFacebook());
    }

    @Override
    public void showMessage(String message) {
        toastMessage(getApplicationContext(), message);
    }

    @Override
    public void armButtonListeners() {
        makeJudgeButton.setOnClickListener(event ->
                presenter.onMakeJudgeClick(updateUserWithFormContent(user)));
        updateButton.setOnClickListener(event ->
                presenter.onUpdateWizardClick(updateUserWithFormContent(user)));
    }



    private void setSpinnerItem(Spinner spinner, ArrayAdapter<String> adapter, String value){
        int position = adapter.getPosition(value);
        spinner.setSelection((position != -1) ? position : 0);
    }

    private User updateUserWithFormContent(User user) {
        user.setDisplayName(displayNameEditText.getText().toString());
        user.setLabel(String.valueOf(labelSpinner.getSelectedItem()));
        user.setTeam(String.valueOf(teamSpinner.getSelectedItem()));
        user.setGender(String.valueOf(genderSpinner.getSelectedItem()));
        user.setFacebook(facebookUrlEditText.getText().toString());

        return user;
    }

    private void initializeSpinners() {
        labelAdapter = attachItems(labelSpinner, VALID_LABELS);
        teamAdapter = attachItems(teamSpinner, VALID_TEAMS);
        genderAdapter = attachItems(genderSpinner, VALID_GENDERS);
    }

    private ArrayAdapter<String> attachItems(Spinner spinner, List<String> data) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, insertNoneItem(data));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        return dataAdapter;
    }

    private List<String> insertNoneItem(List<String> data){
        List<String> result = new ArrayList<>();
        result.add(SPINNER_NONE_ITEM);
        result.addAll(data);

        return result;
    }
}
