package com.homeautomation.homeAutomation.mapper;

public interface Mapper <ClassA, ClassB> {

    ClassB mapTo (ClassA a);
    ClassA mapFrom (ClassB b);
}

