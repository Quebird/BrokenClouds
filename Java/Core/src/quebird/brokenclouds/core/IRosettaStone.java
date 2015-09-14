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

package quebird.brokenclouds.core;

import java.security.MessageDigest;

/**
 * This is a encoder/decoder.
 * 
 * @author Keld Oelykke
 *
 */
public interface IRosettaStone
{
  /**
   * Resets this using the specified password.
   * 
   * @param password
   *          phrase to generate hash key for
   * @param messageDigest
   *          MD5, SHA-1, SHA-256, etc.
   */
  void configure(String password, MessageDigest messageDigest);

  /**
   * Encodes the supplied data.
   * 
   * @param data
   *          data to encode
   * @return encoded data
   */
  byte[] encode(byte[] data);

  /**
   * Decodes the supplied data.
   * 
   * @param data
   *          data to decode
   * @return decoded data
   */
  byte[] decode(byte[] data);

}
