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

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Random;

import quebird.brokenclouds.core.instances.AInstance;

/**
 * Abstract implementation.
 * 
 * @author Keld Oelykke
 *
 */
public abstract class ARosettaStoneV1 extends AInstance implements IRosettaStoneV1
{

  /* (non-Javadoc)
   * @see quebird.brokenclouds.core.stones.v1.IRosettaStoneV1#getAlgorithm()
   */
  @Override
  public String getAlgorithm()
  {
    String result = null;
    
    MessageDigest messageDigest = this.getMessageDigestOrNull();
    if (this.getChecker().isObjectNull(this, messageDigest))
    {
      this.getFailer().failObjectNull(this, "MessageDigestOrNull");
    }
    result = "BC1+" + messageDigest.getAlgorithm();
    
    return result;
  }

  private MessageDigest messageDigestOrNull;
  /* (non-Javadoc)
   * @see quebird.brokenclouds.core.stones.v1.IRosettaStoneV1#getMessageDigestOrNull()
   */
  @Override
  public MessageDigest getMessageDigestOrNull()
  {
    return this.messageDigestOrNull;
  }
  /* (non-Javadoc)
   * @see quebird.brokenclouds.core.stones.v1.IRosettaStoneV1#setMessageDigestOrNull(
   *  java.security.MessageDigest)
   */
  @Override
  public void setMessageDigestOrNull(MessageDigest messageDigestOrNull)
  {
    boolean isRunning = this.isRunning();
    if (this.getChecker().isBooleanTrue(this, isRunning))
    {
      this.getFailer().failBooleanTrue(this, "isRunning");
    }

    this.messageDigestOrNull = messageDigestOrNull;
  }

  private byte[] passwordOrNull;
  /* (non-Javadoc)
   * @see quebird.brokenclouds.core.stones.v1.IRosettaStoneV1#getPasswordOrNull()
   */
  @Override
  public byte[] getPasswordOrNull()
  {
    return this.passwordOrNull;
  }
  /* (non-Javadoc)
   * @see quebird.brokenclouds.core.stones.v1.IRosettaStoneV1#setPasswordOrNull(byte[])
   */
  @Override
  public void setPasswordOrNull(byte[] passwordOrNull)
  {
    boolean isRunning = this.isRunning();
    if (this.getChecker().isBooleanTrue(this, isRunning))
    {
      this.getFailer().failBooleanTrue(this, "isRunning");
    }

    this.passwordOrNull = passwordOrNull;
  }
  
  /* (non-Javadoc)
   * @see quebird.brokenclouds.core.stones.v1.IRosettaStoneV1#start()
   */
  @Override
  public void start()
  {
    // state checks
    byte[] password = this.getPasswordOrNull();
    if (this.getChecker().isObjectNull(this, password))
    {
      this.getFailer().failObjectNull(this, "PasswordOrNull");
    }
    MessageDigest messageDigest = this.getMessageDigestOrNull();
    if (this.getChecker().isObjectNull(this, messageDigest))
    {
      this.getFailer().failObjectNull(this, "MessageDigestOrNull");
    }
    boolean isRunning = this.isRunning();
    if (this.getChecker().isBooleanTrue(this, isRunning))
    {
      this.getFailer().failBooleanTrue(this, "isRunning");
    }

    this.setRunning(true);
  }
  /* (non-Javadoc)
   * @see quebird.brokenclouds.core.stones.v1.IRosettaStoneV1#stop()
   */
  @Override
  public void stop()
  {
    boolean isRunning = this.isRunning();
    if (this.getChecker().isBooleanFalse(this, isRunning))
    {
      this.getFailer().failBooleanFalse(this, "isRunning");
    }

    this.setRunning(false);
  }
  
  private boolean running;
  /* (non-Javadoc)
   * @see quebird.brokenclouds.core.stones.v1.IRosettaStoneV1#isRunning()
   */
  @Override
  public boolean isRunning()
  {
    return this.running;
  }
  
  protected void setRunning(boolean running)
  {
    this.running = running;
  }
  
  
  /* (non-Javadoc)
   * @see quebird.brokenclouds.core.stones.v1.IRosettaStoneV1#encode(byte[], byte[])
   */
  @Override
  public byte[] encode(byte[] salt, byte[] data)
  {
    // argument checks
    if (this.getChecker().isObjectNull(this, salt))
    {
      this.getFailer().failObjectNull(this, "salt");
    }
    if (this.getChecker().isObjectNull(this, data))
    {
      this.getFailer().failObjectNull(this, "data");
    }
    
    // state checks
    byte[] password = this.getPasswordOrNull();
    if (this.getChecker().isObjectNull(this, password))
    {
      this.getFailer().failObjectNull(this, "PasswordOrNull");
    }
    MessageDigest messageDigest = this.getMessageDigestOrNull();
    if (this.getChecker().isObjectNull(this, messageDigest))
    {
      this.getFailer().failObjectNull(this, "MessageDigestOrNull");
    }
    messageDigest.reset();
    messageDigest.update(salt);
    messageDigest.update(password);
    byte[] hash = messageDigest.digest();
    Random random = new Random();
    byte[] availablePaddingBytes = new byte[hash.length];
    random.nextBytes(availablePaddingBytes);
    int indexAvailablePaddingBytes = 0;
   
    int[] order = this.calculateOrderedIndices(hash);
    
    // data length (8 bytes) + enough buckets of hash length to store data.length
    int outputLength = Long.BYTES + ((data.length / hash.length) + 1) * hash.length;
    byte[] output = new byte[outputLength];
    ByteBuffer outputBuffer = ByteBuffer.wrap(output);
    outputBuffer.putLong(0, data.length);
    for (int index = Long.BYTES; index < output.length; ++index)
    {
      int indexDataOffset = index - Long.BYTES;
      int indexHash = indexDataOffset % hash.length;
      // encode data with hash
      byte hashByte = hash[indexHash];
      byte dataByte;
      byte outputByte;
      if (indexDataOffset < data.length)
      {
        dataByte = data[indexDataOffset]; // data - not padding
      }
      else
      {
        dataByte = availablePaddingBytes[indexAvailablePaddingBytes];
        ++indexAvailablePaddingBytes;
      }
      outputByte = (byte) (dataByte + hashByte);
      // shuffle data around according to order of hash bytes
      int offset = indexDataOffset / hash.length;
      int indexOrdered = order[indexHash] + offset * hash.length;
      output[Long.BYTES + indexOrdered] = outputByte;
    }
    
    byte[] result = output;
    
    return result;
  }

  /* (non-Javadoc)
   * @see quebird.brokenclouds.core.stones.v1.IRosettaStoneV1#decode(byte[], byte[])
   */
  @Override
  public byte[] decode(byte[] salt, byte[] data)
  {
    
    // argument checks
    if (this.getChecker().isObjectNull(this, salt))
    {
      this.getFailer().failObjectNull(this, "salt");
    }
    if (this.getChecker().isObjectNull(this, data))
    {
      this.getFailer().failObjectNull(this, "data");
    }
    
    // state checks
    byte[] password = this.getPasswordOrNull();
    if (this.getChecker().isObjectNull(this, password))
    {
      this.getFailer().failObjectNull(this, "PasswordOrNull");
    }
    MessageDigest messageDigest = this.getMessageDigestOrNull();
    if (this.getChecker().isObjectNull(this, messageDigest))
    {
      this.getFailer().failObjectNull(this, "MessageDigestOrNull");
    }
    
    messageDigest.reset();
    messageDigest.update(salt);
    messageDigest.update(password);
    byte[] hash = messageDigest.digest();
    
    int[] order = this.calculateOrderedIndices(hash);
 
    ByteBuffer inputBuffer = ByteBuffer.wrap(data);
    // data length (8 bytes) + enough buckets of hash length to store data.length
    long realDataCount = inputBuffer.getLong(0);
    byte[] output = new byte[(int)realDataCount];
    for (int index = Long.BYTES; index < data.length; ++index)
    {
      int indexDataOffset = index - Long.BYTES;
      int indexHash = indexDataOffset % hash.length;

      // (un)shuffle data around according to order of hash bytes
      int indexOrdered = order[indexHash];
      int offset = (indexDataOffset / hash.length) * hash.length;
      int indexOrderedOffset = indexOrdered + offset;
      if (indexOrderedOffset < realDataCount)
      { // we have data - not random padding
        byte hashByte = hash[indexOrdered];
        int readDataIndex = order[indexOrdered];
        byte dataByte = data[Long.BYTES + readDataIndex + offset];
        byte outputByte = (byte) (dataByte - hashByte);
        output[indexOrderedOffset] = outputByte;
      }
    }

    byte[] result = output;
    
    return result;
  }
  
  
  protected int[] calculateOrderedIndices(byte[] data)
  {
    int[] order = new int[data.length]; // array of indices that would order data
//    for(int i = 0; i < data.length; ++i)
//    {
//      order[i] = i;
//    }
    int orderIndex = 0;
    int orderIndexLast = -1;
    byte orderValue;
    byte orderValueLast = Byte.MIN_VALUE;
    for (int i = 0; i < order.length; ++i)
    {
      orderValue = Byte.MAX_VALUE;
      for (int j = 0; j < data.length; ++j)
      { // find smallest byte value that we haven't yet stored an index of
        byte value = data[j];
        if (orderValueLast == value && orderIndexLast < j)
        { // more of same byte value as last, early exit
          orderValue = value;
          orderIndex = j;
          break;
        }
        else if (orderValueLast < value && value < orderValue)
        { // a bigger byte value than stored last time and it is the smallest in this iteration
          orderValue = value;
          orderIndex = j;
        }
      }
      orderValueLast = orderValue;
      orderIndexLast = orderIndex;
      order[i] = orderIndex;
    }
    
    return order;
  }

}
