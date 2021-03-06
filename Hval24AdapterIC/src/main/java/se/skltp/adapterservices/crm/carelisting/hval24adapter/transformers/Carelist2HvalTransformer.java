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
package se.skltp.adapterservices.crm.carelisting.hval24adapter.transformers;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.module.xml.stax.ReversibleXMLStreamReader;
import org.mule.transformer.AbstractMessageAwareTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;

import se.skl.riv.crm.carelistingresponder.v1.getlisting.GetListingRequestType;
import se.skl.tp.vp.util.helper.PayloadHelper;

public class Carelist2HvalTransformer extends AbstractMessageAwareTransformer
{
	private final Logger log = LoggerFactory.getLogger(getClass());
	private static final JaxbUtil JAXB_UTIL = new JaxbUtil(GetListingRequestType.class);

	public Carelist2HvalTransformer()
    {
        super();
        registerSourceType(Object.class);
        setReturnClass(Object.class);
    }
    
	@Override
	public Object transform(MuleMessage message, String outputEncoding) throws TransformerException {
		try {
			// Get receiver to adress from SOAP Header
			PayloadHelper payloadHelper = new PayloadHelper(message);
			String receiverId = payloadHelper.extractReceiverFromPayload();
			
			log.info("ReceiverId extracted from mule message: {} ",receiverId);
			
			// Position reader in position to read Body element
			ReversibleXMLStreamReader reader = (ReversibleXMLStreamReader)message.getPayload();
			payloadHelper.positionBodyInPayload(reader);
			
			// Transform the XML payload into a JAXB object
			GetListingRequestType request = (GetListingRequestType)JAXB_UTIL.unmarshal(reader);

			// Extract the request information and build a request string applicable for hval
			String personId = request.getPersonId();
			
			// Remove dash in personnummer
			if (personId != null && personId.length() == 13) {
				personId = personId.substring(0, 8) + personId.substring(9,13);
			}

			// Build string
			String payload = "?arg=" + receiverId + personId;

			if(logger.isDebugEnabled()){
				log.debug("Transformed carelist-getListing-request to HVAL request: {}", payload);
			}
			
			return payload;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}