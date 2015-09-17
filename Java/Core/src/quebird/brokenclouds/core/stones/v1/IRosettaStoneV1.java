/////////////////////////////////////////////////////////////////////////////////////////
//
// The MIT License (MIT)
// 
// Copyright (c) 2015 Quebird
// 
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
// 
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
/////////////////////////////////////////////////////////////////////////////////////////

package quebird.brokenclouds.core.stones.v1;

import java.security.MessageDigest;

/**
 * This is a encoder/decoder.
 * 
 * @author Keld Oelykke
 *
 */
public interface IRosettaStoneV1
{

  /**
   * Retrieves the algorithm of this (algorithm + message digest) e.g. BC1+MD5.
   * 
   * @return algorithm of this e.g. BC1+MD5
   */
  String getAlgorithm();

  /**
   * Retrieves the message digest to use for hashing e.g. MD5, SHA-1 or SHA-256.
   * 
   * @return the message digest to use for hashing e.g. MD5, SHA-1 or SHA-256
   */
  MessageDigest getMessageDigestOrNull();

  /**
   * Sets the message digest to use for hashing e.g. MD5, SHA-1 or SHA-256.
   * <p>
   * Cannot be called when running.
   * </p>
   * 
   * @param messageDigestOrNull
   *          the message digest to use for hashing e.g. MD5, SHA-1 or SHA-256
   */
  void setMessageDigestOrNull(MessageDigest messageDigestOrNull);

  /**
   * Retrieves the password to use for hashing.
   * 
   * @return the password to use for hashing
   */
  byte[] getPasswordOrNull();

  /**
   * Sets the password to use for hashing.
   * <p>
   * Cannot be called when running.
   * </p>
   * 
   * @param passwordOrNull
   *          password to use for hashing
   */
  void setPasswordOrNull(byte[] passwordOrNull);

  /**
   * Starts this so encode and decode can be called.
   * <p>
   * This can not be configured while started.
   * </p>
   */
  void start();
  
  /**
   * Stops this so this can be re-configured and re-started.
   */
  void stop();
  
  /**
   * Retrieves the running state of this.
   * 
   * @return true, if running, otherwise false
   */
  boolean isRunning();
  
  /**
   * Encodes the supplied data using message digest, password and salt.
   * <p>
   * Can only be called when running.
   * </p>
   * 
   * @param salt
   *          salt used for hashing
   * @param data
   *          data to encode
   * @return encoded data
   */
  byte[] encode(byte[] salt, byte[] data);

  /**
   * Decodes the supplied data using message digest, password and salt.
   * <p>
   * Can only be called when running.
   * </p>
   * 
   * @param salt
   *          salt used for hashing
   * @param data
   *          data to decode
   * @return decoded data
   */
  byte[] decode(byte[] salt, byte[] data);

}
