/*
 *  NameBearer.java
 *
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Kalina Bontcheva, 17/Sept/2001
 *
 *  $Id: NameBearer.java 15333 2012-02-07 13:18:33Z ian_roberts $
 */

package gate.util;

/** Classes that have features.
  */
public interface NameBearer
{
  /** Sets the name of this resource*/
  public void setName(String name);

  /** Returns the name of this resource*/
  public String getName();

} // interface NameBearer