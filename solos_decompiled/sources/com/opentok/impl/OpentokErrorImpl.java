package com.opentok.impl;

import com.opentok.android.OpentokError;

/* JADX INFO: loaded from: classes15.dex */
public class OpentokErrorImpl extends OpentokError {
    public OpentokErrorImpl(OpentokError.Domain errorDomain, int errorCode) {
        super(errorDomain, errorCode, getErrorDescription(errorCode));
    }

    public static String getErrorDescription(int errorCode) {
        OpentokError.ErrorCode code = OpentokError.ErrorCode.fromTypeCode(errorCode);
        switch (code) {
            case InvalidSessionId:
                return "Unable to connect: an invalid session ID was provided.";
            case AuthorizationFailure:
                return "Authorization Failure - Invalid credentials were provided.";
            case UnknownPublisherInstance:
                return "Cannot unpublish: An unknown Publisher instance was passed into Session.unpublish().";
            case UnknownSubscriberInstance:
                return "Cannot unsubscribe: An unknown Subscriber instance was passed into Session.unsubscribe().";
            case SessionInvalidSignalType:
                return "Invalid signal type.";
            case SessionSignalDataTooLong:
                return "Signal data too long.";
            case SessionSignalTypeTooLong:
                return "Signal type too long.";
            case ConnectionFailed:
                return "Unable to connect to the session: check the network connection.";
            case SessionDisconnected:
                return "Cannot publish: the client is not connected to the OpenTok session.";
            case VideoCaptureFailed:
                return "Video capture has failed";
            case CameraFailed:
                return "The camera of the device has failed. ";
            case VideoRenderFailed:
                return "Video render has failed";
            case SessionNullOrInvalidParameter:
                return "Token null or invalid parameter.";
            case SessionIllegalState:
                return "Unable to connect to a session that is already connected or unable to subscribe to a stream that is no longer in the session.";
            default:
                return "(null description)";
        }
    }
}
