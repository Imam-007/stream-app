package com.stream.app.io;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateResponse {

    private String message;

    private boolean success = false;
}
