package com.nova.tools.utils.guava.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @description:
 * @author: wzh
 * @date: 2022/10/13 19:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EventA {

    private String message;

}

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
class EventB {

    private String message;

}

