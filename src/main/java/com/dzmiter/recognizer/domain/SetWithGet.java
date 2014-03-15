package com.dzmiter.recognizer.domain;

import java.util.Set;

/**
 * Galantis FileNet toolkit
 * Copyright (с ) 2014. All Right Reserved.
 *
 * @author dbezugly
 */
public interface SetWithGet<E> extends Set<E> {
  public E get(int number);
}