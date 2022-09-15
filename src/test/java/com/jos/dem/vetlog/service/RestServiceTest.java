package com.jos.dem.vetlog.service;

import com.jos.dem.vetlog.command.MessageCommand;
import com.jos.dem.vetlog.service.impl.RestServiceImpl;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;

import static org.mockito.Mockito.*;

@Slf4j
class RestServiceTest {

    private RestServiceImpl service;

    @Mock
    private Retrofit retrofit;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        service = new RestServiceImpl(retrofit);
    }

    @Test
    @DisplayName("sending message")
    void shouldSendMessage(TestInfo testInfo) throws IOException {
        log.info("Running: {}", testInfo.getDisplayName());
        RestService restService = mock(RestService.class);
        MessageCommand messageCommand = mock(MessageCommand.class);
        Call<ResponseBody> call = mock(Call.class);

        when(retrofit.create(RestService.class)).thenReturn(restService);
        when(restService.sendMessage(messageCommand)).thenReturn(call);

        service.setup();
        service.sendMessage(messageCommand);

        verify(call).execute();
    }
}
