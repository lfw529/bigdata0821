package com.lfw.flink.flinksql3;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SumCount {
    private int vcSum;
    private int count;
}
