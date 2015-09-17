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

package quebird.brokenclouds.core.instances;

import quebird.brokenclouds.core.overrides.failfast.CallContractor;
import quebird.brokenclouds.core.overrides.failfast.Checker;
import quebird.brokenclouds.core.overrides.failfast.Failer;
import quebird.brokenclouds.core.overrides.failfast.ICallContractor;
import quebird.brokenclouds.core.overrides.failfast.IChecker;
import quebird.brokenclouds.core.overrides.failfast.IFailer;

/**
 * Default implementation of most general instance.
 * 
 * @author Keld Oelykke
 */
public abstract class AInstance implements IInstance
{

  private IChecker checker;
  /**
   * Shortcut to custom checker.
   * 
   * @return custom checker.
   */
  protected IChecker getChecker()
  {
    return this.checker;
  }
  
  protected void setChecker(IChecker checker)
  {
    this.checker = checker;
  }

  private IFailer failer;
  /**
   * Shortcut to custom failer.
   * 
   * @return custom failer.
   */
  protected IFailer getFailer()
  {
    return this.failer;
  }
  
  protected void setFailer(IFailer failer)
  {
    this.failer = failer;
  }
  
  private ICallContractor callContractor;
  /**
   * Shortcut to custom call contractor.
   * 
   * @return custom call contractor.
   */
  protected ICallContractor getCallContractor()
  {
    return this.callContractor;
  }
  
  protected void setCallContractor(ICallContractor callContractor)
  {
    this.callContractor = callContractor;
  }
  
  /**
   * Sets fail-fast fields.
   */
  public AInstance()
  {
    ICallContractor callContractor = new CallContractor();
    IFailer failer = new Failer();
    failer.setCallContractor(callContractor);
    IChecker checker = new Checker();
    checker.setCallContractor(callContractor);
    this.setCallContractor(callContractor);
    this.setChecker(checker);
    this.setFailer(failer);
  }
}
