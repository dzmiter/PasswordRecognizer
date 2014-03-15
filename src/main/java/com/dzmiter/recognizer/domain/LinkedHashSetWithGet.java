package com.dzmiter.recognizer.domain;

import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * Galantis FileNet toolkit
 * Copyright (с ) 2014. All Right Reserved.
 *
 * @author dbezugly
 */
public class LinkedHashSetWithGet<E> extends LinkedHashSet<E> implements SetWithGet<E> {
  public E get(int number) {
    Iterator iterator = iterator();
    int index = 0;
    while (iterator.hasNext()) {
      Object obj = iterator.next();
      if (index == number) return (E) obj;
      index++;
    }
    throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
  }
}