/**
 * *****************************************************************************
 * Copyright 2011-2013 CNES - CENTRE NATIONAL d'ETUDES SPATIALES
 *
 * This file is part of SITools2.
 *
 * SITools2 is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * SITools2 is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with SITools2. If not, see <http://www.gnu.org/licenses/>.
 * ****************************************************************************
 */
package fr.cnes.sitools.extensions.common;

import healpix.core.AngularPosition;
import healpix.tools.CoordTransform;
import java.util.logging.Logger;
import jsky.coords.DMS;
import jsky.coords.HMS;

/**
 * Contains utility methods to store astronomical coordinates.<br/> An AstroCoordinate allows you to store information about a sky position
 *
 * @author Jean-Christophe Malapert <jean-christophe.malapert@cnes.fr>
 */
public class AstroCoordinate {

  /**
   * Logger.
   */
  private static final Logger LOG = Logger.getLogger(AstroCoordinate.class.getName());
  /**
   * Converts hours in degrees.
   */
  public static final double HOUR_TO_DEG = 15.;
  /**
   * Converts degrees in hours.
   */
  public static final double DEG_TO_HOUR = 1. / HOUR_TO_DEG;
  /**
   * Right ascension in degree.
   */
  private double ra;
  /**
   * Declination in degree.
   */
  private double dec;
  /**
   * Coordinate system.
   */
  private CoordinateSystem coordSystem;

  /**
   * List of supported coordinate system.
   */
  public enum CoordinateSystem {

    /**
     * Galactic coordinates.
     */
    GALACTIC,
    /**
     * Equatorial coordinates.
     */
    EQUATORIAL
  }

  protected AstroCoordinate(final AstroCoordinate astro) {
    this.ra = astro.getRaAsDecimal();
    this.dec = astro.getDecAsDecimal();
    this.coordSystem = astro.getCoordinateSystem();
  }

  /**
   * Create a point on the sphere.
   *
   * @param raVal Right ascension in decimal degree.
   * @param decVal Declination in decimal degree.
   */
  public AstroCoordinate(final double raVal, final double decVal) {
    this.ra = raVal;
    this.dec = decVal;
    this.coordSystem = CoordinateSystem.EQUATORIAL;
  }

  /**
   * Create a point on the sphere.
   *
   * @param raStr Right ascension in sexagesimal in format H:M:S.sss or H M S.sss.
   * @param decStr Declination in sexagesimal in format D:M:S.sss or D M S.sss.
   * @RuntimeException - if coordinates format is not valid
   * @see The conversion from sexagesimal to decimal coordinates is done by the use of <a href="http://jsky.sourceforge.net/">jsky</a>.
   */
  public AstroCoordinate(final String raStr, final String decStr) {
    this.ra = parseRa(raStr);
    this.dec = parseDec(decStr);
    this.coordSystem = CoordinateSystem.EQUATORIAL;
  }

  /**
   * Returns the right ascension as decimal degrees.
   *
   * @param raStr Right ascension in format H:M:S.sss or H M S.sss
   * @return the right ascension as decimal degrees
   */
  public static double parseRa(final String raStr) {
    String raSexa = raStr;
    raSexa = raSexa.replaceAll("\\s+", ":");
    HMS hms = new HMS(raSexa);
    return hms.getVal() * HOUR_TO_DEG;
  }

  /**
   * Returns the declination as decimal degrees.
   *
   * @param decStr Declination in format H:M:S.sss or H M S.sss
   * @return the declinatino as decimal degrees
   */
  public static double parseDec(final String decStr) {
    String decSexa = decStr;
    decSexa = decSexa.replaceAll("\\s+", ":");
    DMS dms = new DMS(decSexa);
    return dms.getVal();
  }

  /**
   * Returns the right ascension as decimal degree.
   *
   * @return right ascension.
   */
  public final double getRaAsDecimal() {
    return this.ra;
  }

  /**
   * Returns the declination as decimal degree.
   *
   * @return declination.
   */
  public final double getDecAsDecimal() {
    return this.dec;
  }

  /**
   * Returns the right ascension as sexagesimal.
   *
   * @return right ascension.
   * @RuntimeException - if coordinate format is not valid
   */
  public final String getRaAsSexagesimal() {
    HMS hms = new HMS(this.ra * DEG_TO_HOUR);
    return hms.toString(true);
  }

  /**
   * Returns the declination as sexagesimal.
   *
   * @return declination.
   * @RuntimeException - if coordinate format is not valid
   */
  public final String getDecAsSexagesimal() {
    DMS dms = new DMS(this.dec);
    return dms.toString(true);
  }

  /**
   * Returns the coordinate system that is used for the coordinates.
   *
   * @return the coordinate system that is used for the coordinates
   */
  public final CoordinateSystem getCoordinateSystem() {
    return this.coordSystem;
  }

  /**
   * Sets the right ascension.
   *
   * @param raVal right ascension in decimal degree.
   */
  public final void setRaAsDecimal(final double raVal) {
    this.ra = raVal;
  }

  /**
   * Sets the declination.
   *
   * @param decVal declination in decimal degree.
   */
  public final void setDecAsDecimal(final double decVal) {
    this.dec = decVal;
  }

  /**
   * Sets the right ascension in decimal degree.
   *
   * @param raStr right ascension in sexagesimal in format HH:MM:SS.sss.
   * @RuntimeException - if coordinate format is not valid
   * @see The conversion from sexagesimal to decimal coordinates is done by the use of <a href="http://jsky.sourceforge.net/">jsky</a>.
   */
  public final void setRaAsSexagesimal(final String raStr) {
    HMS hms = new HMS(raStr);
    this.ra = hms.getVal() * HOUR_TO_DEG;
  }

  /**
   * Sets the coordinate system that is used for the stored coordinates.
   *
   * @param coordSystemVal the coordinate system that is used for the stored coordinates
   */
  public final void setCoordinateSystem(final CoordinateSystem coordSystemVal) {
    this.coordSystem = coordSystemVal;
  }

  /**
   * Sets the declination in decimal degree.
   *
   * @param decStr declination in sexagesimal.
   * @RuntimeException - if coordinate format is not valid
   * @see The conversion from sexagesimal to decimal coordinates is done by the use of <a href="http://jsky.sourceforge.net/">jsky</a>.
   */
  public final void setDecAsSexagesimal(final String decStr) {
    DMS dms = new DMS(decStr);
    this.dec = dms.getVal();
  }

  /**
   * Converts the sky position of an object from a Equatorial reference frame to galactic frame when neeeded.
   *
   * <p> if the coordinates of
   * <code>astroCoord</code> are not in Equatorial frame, then a IllegalArgumentException is thrown. </p>
   *
   * @param astroCoord Sky object position in Equatorial reference frame
   * @param coordinateSystem final reference frame
   * @throws NameResolverException if the transformation Equatorial to galactic failed
   */
  public final AstroCoordinate transformTo(CoordinateSystem coodSystem) {
    AstroCoordinate result;
    AngularPosition angularPosition = new AngularPosition(getDecAsDecimal(), getRaAsDecimal());
    switch (coodSystem) {
      case EQUATORIAL:
        if (getCoordinateSystem() == CoordinateSystem.GALACTIC) {
          try {
            angularPosition = CoordTransform.transformInDeg(angularPosition, CoordTransform.GAL2EQ);
            result = new AstroCoordinate(angularPosition.phi(), angularPosition.theta());
            result.setCoordinateSystem(coodSystem);
          } catch (Exception ex) {
            throw new RuntimeException(ex);
          }
        } else {
          result = new AstroCoordinate(this);
        }
        break;
      case GALACTIC:
        if (getCoordinateSystem() == CoordinateSystem.EQUATORIAL) {
          try {
            angularPosition = CoordTransform.transformInDeg(angularPosition, CoordTransform.EQ2GAL);
            result = new AstroCoordinate(angularPosition.phi(), angularPosition.theta());
            result.setCoordinateSystem(coodSystem);
          } catch (Exception ex) {
            throw new RuntimeException(ex);
          }
        } else {
          result = new AstroCoordinate(this);
        }
        break;
      default:
        throw new RuntimeException(coodSystem + " is not supported.");
    }
    return result;
  }

  public final void processTo(CoordinateSystem coordSystem) {
    AngularPosition angularPosition = new AngularPosition(getDecAsDecimal(), getRaAsDecimal());
    switch (coordSystem) {
      case EQUATORIAL:
        if (getCoordinateSystem() == CoordinateSystem.GALACTIC) {
          try {
            angularPosition = CoordTransform.transformInDeg(angularPosition, CoordTransform.GAL2EQ);
            setRaAsDecimal(angularPosition.phi);
            setDecAsDecimal(angularPosition.theta());
            setCoordinateSystem(coordSystem);
          } catch (Exception ex) {
            throw new RuntimeException(ex);
          }
        } 
        break;
      case GALACTIC:
        if (getCoordinateSystem() == CoordinateSystem.EQUATORIAL) {
          try {
            angularPosition = CoordTransform.transformInDeg(angularPosition, CoordTransform.EQ2GAL);
            setRaAsDecimal(angularPosition.phi);
            setDecAsDecimal(angularPosition.theta());
            setCoordinateSystem(coordSystem);
          } catch (Exception ex) {
            throw new RuntimeException(ex);
          }
        } 
        break;
      default:
        throw new RuntimeException(coordSystem + " is not supported.");
    }   
  }

  /**
   * Displays the coordinates as follows: Coordinate: (ra_decimal,dec_decimal) or (ra_sexagecimal,dec_sexagesimal).
   *
   * @return Returns a String representation of the object
   */
  @Override
  public final String toString() {
    return "Coordinate: (" + getRaAsDecimal() + "," + getDecAsDecimal() + ") or (" + getRaAsSexagesimal() + ","
            + getDecAsSexagesimal() + ")";
  }
}