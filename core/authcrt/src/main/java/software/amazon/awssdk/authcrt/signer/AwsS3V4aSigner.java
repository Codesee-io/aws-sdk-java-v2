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

package software.amazon.awssdk.authcrt.signer;

import java.util.Optional;
import software.amazon.awssdk.annotations.SdkPublicApi;
import software.amazon.awssdk.auth.signer.S3SignerExecutionAttribute;
import software.amazon.awssdk.authcrt.signer.internal.BaseCrtAws4aSigner;
import software.amazon.awssdk.authcrt.signer.params.Aws4aPresignerParams;
import software.amazon.awssdk.authcrt.signer.params.AwsS3V4aSignerParams;
import software.amazon.awssdk.core.interceptor.ExecutionAttributes;
import software.amazon.awssdk.crt.auth.signing.AwsSigningConfig;
import software.amazon.awssdk.http.SdkHttpFullRequest;


@SdkPublicApi
public class AwsS3V4aSigner extends BaseCrtAws4aSigner<AwsS3V4aSignerParams, Aws4aPresignerParams> {

    private AwsS3V4aSigner() {
    }

    public static AwsS3V4aSigner create() {
        return new AwsS3V4aSigner();
    }

    @Override
    public SdkHttpFullRequest sign(SdkHttpFullRequest request, ExecutionAttributes executionAttributes) {
        AwsS3V4aSignerParams signingParams = buildSignerParams(executionAttributes);
        try (AwsSigningConfig signingConfig = createCrtSigningConfig(signingParams)) {
            return signWithCrt(request, signingConfig);
        }
    }

    private AwsS3V4aSignerParams buildSignerParams(ExecutionAttributes executionAttributes) {
        AwsS3V4aSignerParams.Builder signingParams = extractSignerParams(AwsS3V4aSignerParams.builder(), executionAttributes);

        Optional.ofNullable(executionAttributes.getAttribute(S3SignerExecutionAttribute.ENABLE_PAYLOAD_SIGNING))
                .ifPresent(signingParams::enablePayloadSigning);

        return signingParams.build();

    }

    protected AwsSigningConfig createCrtSigningConfig(AwsS3V4aSignerParams signingParams) {
        AwsSigningConfig signingConfig = new AwsSigningConfig();

        fillInCrtSigningConfig(signingConfig, signingParams);

        if (signingParams.enablePayloadSigning()) {
            signingConfig.setSignedBodyValue(AwsSigningConfig.AwsSignedBodyValueType.PAYLOAD);
            signingConfig.setSignedBodyHeader(AwsSigningConfig.AwsSignedBodyHeaderType.X_AMZ_CONTENT_SHA256);
        } else {
            signingConfig.setSignedBodyValue(AwsSigningConfig.AwsSignedBodyValueType.UNSIGNED_PAYLOAD);
            signingConfig.setSignedBodyHeader(AwsSigningConfig.AwsSignedBodyHeaderType.NONE);
        }

        signingConfig.setSignatureType(AwsSigningConfig.AwsSignatureType.HTTP_REQUEST_VIA_HEADERS);

        return signingConfig;
    }

}
