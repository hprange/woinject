/**
 * Copyright (C) 2010 hprange <hprange@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.woinject;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;

/**
 * Experiment to avoid inheritance of Application, Session, DirectAction and
 * EnterpriseObject classes.
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @since 1.0
 */
public class Runner
{
	public static void main(String[] args) throws Throwable
	{
		ClassPool cp = ClassPool.getDefault();
		Loader cl = new Loader(cp);
		CtClass cc = cp.get("com.webobjects.foundation._NSUtilities");
		CtMethod m = cc.getDeclaredMethod("instantiateObject");
		m.insertBefore("{ System.out.println(\"\tClass: \"+$1+\" Parameters: \"+java.util.Arrays#toString($2)+\"\"); }");

		Thread.currentThread().setContextClassLoader(cl);

		cl.run("br.com.doit.Application", args);
	}
}
