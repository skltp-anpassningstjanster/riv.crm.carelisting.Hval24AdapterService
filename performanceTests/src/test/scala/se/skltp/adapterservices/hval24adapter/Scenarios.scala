/**
 * Copyright (c) 2014, Inera AB <http://www.inera.se/>. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package se.skltp.adapterservices.hval24adapter  

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import scala.concurrent.duration._

object Scenarios {

    val rampUpTimeSecs = 10
    val minWaitMs      = 500 milliseconds
    val maxWaitMs      = 1500 milliseconds

	/*
	 *	HTTPS scenarios
     */	
	
	// GetListing OK
    val scn_GetListingOk = scenario("GetListing OK https scenario")
      .during(Conf.testTimeSecs) {     
        exec(
          http("GetListing OK")
            .post("/vp/GetListing/1/rivtabp20")
            .headers(Headers.getListingHttps_header)
            .body(RawFileBody("data/GetListing.xml")).asXML
            .check(status.is(200))
          )
        .pause(minWaitMs, maxWaitMs)
    }
}