/*
 *  AnaphorTagger.java
 *
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Valentin Tablan 19 Jan 2012
 *  
 *  $Id: Tagger.java 15568 2012-03-09 14:15:23Z valyt $
 */
package gate.creole.coref;

import gate.Annotation;
import gate.creole.ResourceInstantiationException;

import java.util.List;
import java.util.Set;

public interface Tagger {
  
  /**
   * Gets the annotation type this {@link Tagger} instance is used for.
   * @return
   */
  public String getAnnotationType();
  
  public Set<String> tag(Annotation[] anaphors, int anaphor, CorefBase owner);
  
  public void init(CorefBase owner) throws ResourceInstantiationException;
  
}
