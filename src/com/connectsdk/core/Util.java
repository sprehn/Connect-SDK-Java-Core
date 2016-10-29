/*
 * Util
 * Connect SDK
 * 
 * Copyright (c) 2014 LG Electronics.
 * Created by Jeffrey Glenn on 27 Feb 2014
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.connectsdk.core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.conn.util.InetAddressUtils;

import com.connectsdk.service.capability.listeners.ErrorListener;
import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.ServiceCommandError;

import android.content.Context;

public final class Util {
    static public String T = "Connect SDK";

    static private ExecutorService executor;
    static private InetAddress localIP;

    /**
     * Configure Util on component start.
     *
     * @param e must not be <code>null</null>
     * @param ip may be <code>null</code>
     */
    public static void start(ExecutorService e, InetAddress ip) {
        executor = e;
        localIP = ip;
    }

    public static void stop() {
        executor = null;
        localIP = null;
    }

    public static void runOnUI(Runnable runnable) {
        // no UI in openhab
        runInBackground(runnable, true);
    }

    public static void runInBackground(Runnable runnable, boolean forceNewThread) {
        executor.execute(runnable);
    }

    public static void runInBackground(Runnable runnable) {
        runInBackground(runnable, false);
    }

    public static Executor getExecutor() {
        return executor;
    }

    public static boolean isMain() {
        return true;
    }

    public static <T> void postSuccess(final ResponseListener<T> listener, final T object) {
        if (listener == null)
            return;

        Util.runOnUI(new Runnable() {

            @Override
            public void run() {
                listener.onSuccess(object);
            }
        });
    }

    public static void postError(final ErrorListener listener, final ServiceCommandError error) {
        if (listener == null)
            return;

        Util.runOnUI(new Runnable() {

            @Override
            public void run() {
                listener.onError(error);
            }
        });
    }

    public static byte[] convertIpAddress(int ip) {
        return new byte[] {
                (byte) (ip & 0xFF), 
                (byte) ((ip >> 8) & 0xFF), 
                (byte) ((ip >> 16) & 0xFF), 
                (byte) ((ip >> 24) & 0xFF)};
    }

    public static long getTime() {
        return TimeUnit.MILLISECONDS.toSeconds(new Date().getTime());
    }

    public static boolean isIPv4Address(String ipAddress) {
        return InetAddressUtils.isIPv4Address(ipAddress);
    }

    public static boolean isIPv6Address(String ipAddress) {
        return InetAddressUtils.isIPv6Address(ipAddress);
    }

    public static InetAddress getIpAddress(Context context) throws UnknownHostException {
        return localIP;
    }
}