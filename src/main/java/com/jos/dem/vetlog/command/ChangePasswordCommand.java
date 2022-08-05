/*
Copyright 2022 Jose Morales joseluis.delacruz@gmail.com
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

package com.jos.dem.vetlog.command;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ChangePasswordCommand implements Command {

    @NotNull
    @Size(min=36, max=36)
    private String token;

    @NotNull
    @Size(min=8, max=50)
    private String password;

    @NotNull
    @Size(min=8, max=50)
    private String passwordConfirmation;
}
