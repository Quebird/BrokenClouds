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
    
    ByteBuffer byteBuffer = ByteBuffer.allocate(salt.length + password.length);
    byteBuffer.put(salt, 0, salt.length);
    byteBuffer.put(password, 0, password.length);
    byte[] hash = messageDigest.digest(byteBuffer.array());
    
    byte[] output = new byte[data.length];
    for (int index = 0; index < data.length; ++index)
    {
      byte hashByte = hash[index % hash.length];
      byte dataByte = data[index];
      byte outputByte = (byte) (dataByte + hashByte);
      output[index] = outputByte;
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
    
    ByteBuffer byteBuffer = ByteBuffer.allocate(salt.length + password.length);
    byteBuffer.put(salt, 0, salt.length);
    byteBuffer.put(password, 0, password.length);
    byte[] hash = messageDigest.digest(byteBuffer.array());
    
    byte[] output = new byte[data.length];
    for (int index = 0; index < data.length; ++index)
    {
      byte hashByte = hash[index % hash.length];
      byte dataByte = data[index];
      byte outputByte = (byte) (dataByte - hashByte);
      output[index] = outputByte;
    }

    byte[] result = output;
    
    return result;
  }

}
