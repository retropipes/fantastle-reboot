/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.puttysoftware.fantastlereboot.game;

/**
 *
 * @author wrldwzrd89
 */
public class InfiniteRecursionException extends RuntimeException {
  private static final long serialVersionUID = 54364343055203034L;

  /**
   * Creates a new instance of <code>InfiniteRecursionException</code> without
   * detail message.
   */
  public InfiniteRecursionException() {
  }

  /**
   * Constructs an instance of <code>InfiniteRecursionException</code> with the
   * specified detail message.
   *
   * @param msg
   *          the detail message.
   */
  public InfiniteRecursionException(final String msg) {
    super(msg);
  }
}
