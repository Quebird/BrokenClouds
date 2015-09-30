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

package quebird.brokenclouds.core.unit.stones.v1;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import quebird.brokenclouds.core.stones.v1.IRosettaStoneV1;
import quebird.brokenclouds.core.stones.v1.RosettaStoneV1;

/**
 * Tests RosettaStoneV1.
 * 
 * @author Keld Oelykke
 */
public class RosettaStoneV1Test
{
  final String[] charsetNames = new String[]
  {
      "UTF-8",
      "UTF-16"
  };  
  final String[] digestAlgorithmNames = new String[]
  {
      "MD5",
      "SHA-1",
      "SHA-256"
  };
  
  @Test
  public void testEncodeAndDecode() throws NoSuchAlgorithmException
  {
    for (int i = 0; i < 2; ++i) // bunch test
    {
      for (String digestAlgorithmName : digestAlgorithmNames)
      {
        for (String charsetName : charsetNames)
        {
          String stringData = "Hello World! Hellø Wørld! 0123456789 §$!#€%&/()=?+´`¨^*'-_.:,;<>";
          System.out.println(this + ".testEncodeAndDecode(): \n\t#" + i + ": "
              + digestAlgorithmName + " + " 
              + charsetName + " on '" + stringData + "'");
  
          MessageDigest messageDigest = MessageDigest.getInstance(digestAlgorithmName);
          Charset charset = Charset.forName(charsetName);
          
          IRosettaStoneV1 rosettaStone = new RosettaStoneV1();
          rosettaStone.setMessageDigestOrNull(messageDigest);
          rosettaStone.setPasswordOrNull("password".getBytes(charset));
          rosettaStone.start();
          UUID uuid = UUID.randomUUID();
          byte[] data = stringData.getBytes(charset);
          byte[] salt = uuid.toString().getBytes(charset);
          byte[] encodedData = rosettaStone.encode(salt, data);
          
          // that was a simple encoding with byte shuffling
          // can we split this encoded data to a set of open servers?
          // say we have server sets A, B, C, ... each set has data replication
          // if the number of sets we use is X
          // we send bytes 0,X,2*X,... to A, 1,X+1,2*X+1,.. to B, etc. 
          // the distributed byte sets will be accompanied with 
          // an unique user id, salt, algorithm version, date, etc.
          byte[] decodedData = rosettaStone.decode(salt, encodedData);
          String stringDataDecoded = new String(decodedData, charset);
          Assert.assertEquals(stringData, stringDataDecoded);
        }
      }
    }
  }

}
