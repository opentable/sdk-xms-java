package com.clxcommunications.xms;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.clxcommunications.xms.api.DeliveryStatus;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;

@RunWith(JUnitQuickcheck.class)
public class BatchDeliveryReportParamsTest {

	@Test
	public void generatesExpectedQueryParametersMinimal() throws Exception {
		BatchDeliveryReportParams filter =
		        ClxApi.batchDeliveryReportParams().build();

		List<NameValuePair> actual = filter.toQueryParams();

		assertThat(actual, is(empty()));
	}

	@Test
	public void generatesExpectedQueryParametersMaximalish() throws Exception {
		BatchDeliveryReportParams filter =
		        ClxApi.batchDeliveryReportParams()
		                .fullReport()
		                .addStatus(DeliveryStatus.EXPIRED,
		                        DeliveryStatus.DELIVERED)
		                .addCode(100, 200, 300)
		                .build();

		List<NameValuePair> actual = filter.toQueryParams();

		assertThat(actual, containsInAnyOrder(
		        (NameValuePair) new BasicNameValuePair("type", "full"),
		        new BasicNameValuePair("status", "Expired,Delivered"),
		        new BasicNameValuePair("code", "100,200,300")));
	}

	@Property
	public void generatesValidQueryParameters(
	        BatchDeliveryReportParams.ReportType reportType, Set<Integer> codes)
	        throws Exception {
		BatchDeliveryReportParams filter =
		        ClxApi.batchDeliveryReportParams()
		                .reportType(reportType)
		                .addStatus(DeliveryStatus.EXPIRED,
		                        DeliveryStatus.DELIVERED)
		                .codes(codes)
		                .build();

		List<NameValuePair> params = filter.toQueryParams();

		// Will throw IllegalArgumentException if an invalid URI is attempted.
		new URIBuilder().addParameters(params).build();
	}

}
