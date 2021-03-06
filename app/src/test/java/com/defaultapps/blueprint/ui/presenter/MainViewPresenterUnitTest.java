package com.defaultapps.blueprint.ui.presenter;

import android.util.Log;

import com.defaultapps.blueprint.data.interactor.MainViewInteractor;
import com.defaultapps.blueprint.ui.fragment.MainViewImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class MainViewPresenterUnitTest {

    private MainViewPresenterImpl mainViewPresenter;

    private List<String> data1;
    private List<String> data2;

    @Mock
    MainViewInteractor mainViewInteractor;

    @Mock
    MainViewImpl mainView;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(Log.class);
        mainViewPresenter = new MainViewPresenterImpl(mainViewInteractor);
        mainViewPresenter.setView(mainView);
        data1 = new ArrayList<>();
        data2 = new ArrayList<>();

    }
    @Test
    public void requestUpdateTest() throws Exception {
        mainViewPresenter.requestUpdate();

        verify(mainView).showLoading();
        verify(mainView).hidePhotosList();
        verify(mainView).hideError();
    }

    @Test
    public void requestLocalDataTest() throws Exception {
        mainViewPresenter.requestCachedData();

        verify(mainView).showLoading();
    }

    @Test
    public void onFailureTest() throws Exception {
        mainViewPresenter.onFailure();

        verify(mainView).hideLoading();
        verify(mainView).hidePhotosList();
        verify(mainView).showError();
    }

    @Test
    public void onSuccessTest() throws Exception {
        mainViewPresenter.onSuccess(data1, data2);

        verify(mainView).hideLoading();
        verify(mainView).hideError();
        verify(mainView).showPhotosList();
        verify(mainView).updateView(data1, data2);
    }

    @Test
    public void detachViewTest() throws Exception {
        mainViewPresenter.detachView();

    }

    /**
     * Testing config changes behavior.
     * In this test case data is still loading, so when config appears progressBar will be shown.
     * @throws Exception
     */
    @Test
    public void restoreViewStateTestLoading() throws Exception {
        mainViewPresenter.setTaskStatus(true);

        mainViewPresenter.restoreViewState();

        verify(mainView).showLoading();
        verify(mainView).hidePhotosList();
        verify(mainView).hideError();
    }

    /**
     * Testing config changes behavior.
     * In this test case data is failed to load and error screen displayed.
     * @throws Exception
     */
    @Test
    public void restoreViewStateTestError() throws Exception {
        mainViewPresenter.setTaskStatus(false);
        mainViewPresenter.setErrorVisibilityStatus(true);
        mainViewPresenter.restoreViewState();

        verify(mainView).showError();
        verify(mainView).hideLoading();
        verify(mainView).hidePhotosList();
    }

    /**
     * Config changes behavior.
     * If taskRunning == false and errorVisible == false when latest data should be displayed.
     */
    @Test
    public void restoreViewState() {
        mainViewPresenter.setTaskStatus(false);
        mainViewPresenter.setErrorVisibilityStatus(false);
        mainViewPresenter.restoreViewState();

        verify(mainView).showLoading();
        verify(mainViewInteractor).loadDataFromCache();
    }


}