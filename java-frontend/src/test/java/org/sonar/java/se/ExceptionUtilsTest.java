/*
 * SonarQube Java
 * Copyright (C) 2012-2018 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.java.se;

import org.junit.Test;
import org.sonar.java.ast.parser.JavaParser;
import org.sonar.java.bytecode.loader.SquidClassLoader;
import org.sonar.java.resolve.SemanticModel;
import org.sonar.java.resolve.Symbols;
import org.sonar.plugins.java.api.semantic.Type;
import org.sonar.plugins.java.api.tree.CompilationUnitTree;

import java.io.File;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ExceptionUtilsTest {

  @Test
  public void test_is_unchecked_exception() {
    assertThat(ExceptionUtils.isUncheckedException(null)).isFalse();
    assertThat(ExceptionUtils.isUncheckedException(Symbols.unknownType)).isFalse();
    SemanticModel semanticModel = getSemanticModel();
    Type ex = semanticModel.getClassType("java.lang.IllegalArgumentException");
    assertThat(ExceptionUtils.isUncheckedException(ex)).isTrue();
    ex = semanticModel.getClassType("java.lang.Exception");
    assertThat(ExceptionUtils.isUncheckedException(ex)).isTrue();
    ex = semanticModel.getClassType("java.lang.Throwable");
    assertThat(ExceptionUtils.isUncheckedException(ex)).isTrue();
  }

  private SemanticModel getSemanticModel() {
    SquidClassLoader squidClassLoader = new SquidClassLoader(new ArrayList<>());
    File file = new File("src/test/java/org/sonar/java/se/ExceptionUtilsTest.java");
    CompilationUnitTree tree = (CompilationUnitTree) JavaParser.createParser().parse(file);
    return SemanticModel.createFor(tree, squidClassLoader);
  }
}
