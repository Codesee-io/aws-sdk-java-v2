/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package software.amazon.awssdk.authcrt.signer.params;

import java.time.Instant;
import software.amazon.awssdk.annotations.SdkPublicApi;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.utils.Validate;

/**
 * Parameters that are used during Sigv4a signing.
 *
 * Required parameters vary based on signer implementations. Signer implementations might only use a
 * subset of params in this class.
 */
@SdkPublicApi
public class Aws4aSignerParams {
    private final Boolean doubleUrlEncode;
    private final AwsCredentials awsCredentials;
    private final String signingName;
    private final String signingRegionSet;
    private final Instant signingTimestamp;

    Aws4aSignerParams(BuilderImpl<?> builder) {
        this.doubleUrlEncode = Validate.paramNotNull(builder.doubleUrlEncode, "Double Url encode");
        this.awsCredentials = Validate.paramNotNull(builder.awsCredentials, "Credentials");
        this.signingName = Validate.paramNotNull(builder.signingName, "service signing name");
        this.signingRegionSet = Validate.paramNotNull(builder.signingRegionSet, "signing region set");
        this.signingTimestamp = builder.signingTimestamp;
    }

    public static Builder builder() {
        return new BuilderImpl<>();
    }

    public Boolean doubleUrlEncode() {
        return doubleUrlEncode;
    }

    public AwsCredentials awsCredentials() {
        return awsCredentials;
    }

    public String signingName() {
        return signingName;
    }

    public String signingRegionSet() {
        return signingRegionSet;
    }

    public Instant signingTimestamp() {
        return signingTimestamp;
    }

    public interface Builder<B extends Builder> {

        /**
         * Set this value to double url-encode the resource path when constructing the
         * canonical request.
         *
         * By default, all services except S3 enable double url-encoding.
         *
         * @param doubleUrlEncode Set true to enable double url encoding. Otherwise false.
         */
        B doubleUrlEncode(Boolean doubleUrlEncode);

        /**
         * Sets the aws credentials to use for computing the signature.
         *
         * @param awsCredentials Aws Credentials to use for computing the signature.
         */
        B awsCredentials(AwsCredentials awsCredentials);

        /**
         * The name of the AWS service to be used for computing the signature.
         *
         * @param signingName Name of the AWS service to be used for computing the signature.
         */
        B signingName(String signingName);

        /**
         * The AWS region set to be used while computing the Sigv4a signature.
         *
         * @param signingRegionSet AWS region set to be used while computing the Sigv4a signature.
         */
        B signingRegionSet(String signingRegionSet);

        /**
         * The timestamp to use when computing the signing date for the request.
         *
         * @param signingTimestamp The timestamp to use when computing the signing date for the request.
         */
        B signingTimestamp(Instant signingTimestamp);

        Aws4aSignerParams build();
    }

    protected static class BuilderImpl<B extends Builder> implements Builder<B> {
        private static final Boolean DEFAULT_DOUBLE_URL_ENCODE = Boolean.TRUE;

        private Boolean doubleUrlEncode = DEFAULT_DOUBLE_URL_ENCODE;
        private AwsCredentials awsCredentials;
        private String signingName;
        private String signingRegionSet;
        private Instant signingTimestamp;

        protected BuilderImpl() {

        }

        @Override
        public B doubleUrlEncode(Boolean doubleUrlEncode) {
            this.doubleUrlEncode = doubleUrlEncode;
            return (B) this;
        }

        public void setDoubleUrlEncode(Boolean doubleUrlEncode) {
            doubleUrlEncode(doubleUrlEncode);
        }

        @Override
        public B awsCredentials(AwsCredentials awsCredentials) {
            this.awsCredentials = awsCredentials;
            return (B) this;
        }

        public void setAwsCredentials(AwsCredentials awsCredentials) {
            awsCredentials(awsCredentials);
        }

        @Override
        public B signingName(String signingName) {
            this.signingName = signingName;
            return (B) this;
        }

        public void setSigningName(String signingName) {
            signingName(signingName);
        }

        @Override
        public B signingRegionSet(String signingRegionSet) {
            this.signingRegionSet = signingRegionSet;
            return (B) this;
        }

        public void setSigningRegion(String signingRegionSet) {
            signingRegionSet(signingRegionSet);
        }

        @Override
        public B signingTimestamp(Instant signingTimestamp) {
            this.signingTimestamp = signingTimestamp;
            return (B) this;
        }

        public void setTimeOffset(Instant signingTimestamp) {
            signingTimestamp(signingTimestamp);
        }

        @Override
        public Aws4aSignerParams build() {
            return new Aws4aSignerParams(this);
        }
    }
}
