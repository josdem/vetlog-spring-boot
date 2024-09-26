/*
Copyright 2024 Jose Morales contact@josdem.io

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.josdem.vetlog.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.josdem.vetlog.command.MessageCommand;
import com.josdem.vetlog.service.impl.RestServiceImpl;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import retrofit2.Call;
import retrofit2.Retrofit;

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
        var restService = mock(RestService.class);
        var messageCommand = mock(MessageCommand.class);
        var call = mock(Call.class);

        when(retrofit.create(RestService.class)).thenReturn(restService);
        when(restService.sendMessage(messageCommand)).thenReturn(call);

        service.setup();
        service.sendMessage(messageCommand);

        verify(call).execute();
    }
}
