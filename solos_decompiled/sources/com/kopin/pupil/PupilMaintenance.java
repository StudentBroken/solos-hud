package com.kopin.pupil;

import android.util.Log;
import com.kopin.accessory.base.Connection;
import com.kopin.accessory.base.Packet;
import com.kopin.accessory.base.PacketReceivedListener;
import com.kopin.accessory.base.PacketType;
import com.kopin.accessory.packets.ActionType;
import com.kopin.accessory.packets.FlashResponseContent;
import com.kopin.accessory.packets.base.ActionPacketContent;
import com.kopin.accessory.utility.Flash;

/* JADX INFO: loaded from: classes25.dex */
public class PupilMaintenance extends PupilDevice {
    private static MaintenanceModeListener mListener;
    private static int mCurrentBootBank = 0;
    private static byte mCurrentFlashOptions = 0;
    private static byte FLASH_OPTS_PRE_ERASE = 1;
    private static byte FLASH_OPTS_PROGRESS_MASK = -2;
    private static byte FLASH_OPTS_PROGRESS_MAX = 127;
    public static int FLASH_OPTS_NO_FLASH_PROGRESS = 0;
    private static final PacketReceivedListener mFlashListener = new PacketReceivedListener() { // from class: com.kopin.pupil.PupilMaintenance.1
        private boolean onPower = false;

        @Override // com.kopin.accessory.base.PacketReceivedListener
        public void onPacketReceived(Connection connection, Packet packet) {
            switch (AnonymousClass2.$SwitchMap$com$kopin$accessory$base$PacketType[packet.getType().ordinal()]) {
                case 1:
                    FlashResponseContent resp = (FlashResponseContent) packet.getContent();
                    FlashResponseContent.ResponseType type = resp.getType();
                    int data = resp.getData();
                    Log.d("Flash Response", String.format("type = %02x, data = %06x (raw %08x)", Integer.valueOf(type.ordinal()), Integer.valueOf(data), Integer.valueOf(resp.getRawValue())));
                    switch (AnonymousClass2.$SwitchMap$com$kopin$accessory$packets$FlashResponseContent$ResponseType[type.ordinal()]) {
                        case 1:
                            int unused = PupilMaintenance.mCurrentBootBank = data;
                            if (PupilMaintenance.mListener != null) {
                                PupilMaintenance.mListener.onReconnection(data == 2);
                            }
                            break;
                        case 2:
                            if (PupilMaintenance.mListener != null) {
                                PupilMaintenance.mListener.onFlashProgress(data);
                            }
                            break;
                        case 3:
                            if (PupilMaintenance.mListener != null) {
                                PupilMaintenance.mListener.onFlashComplete(data);
                            }
                            break;
                        case 4:
                        case 6:
                        case 7:
                            break;
                        case 5:
                            if (PupilMaintenance.mListener != null) {
                                PupilMaintenance.mListener.onFlashErased();
                            }
                            break;
                        case 8:
                            if (PupilMaintenance.mListener != null) {
                                if (data < 2) {
                                    PupilMaintenance.mListener.onVerified(data == 0);
                                } else {
                                    PupilMaintenance.mListener.onChecksum(data == 2);
                                }
                            }
                            break;
                        case 9:
                            if (PupilMaintenance.mListener != null) {
                                PupilMaintenance.mListener.onFlashError(resp.getErrorCode());
                            }
                            break;
                        default:
                            Log.d("Flash Response", "  Unexpected type, ignoring...");
                            break;
                    }
                    break;
                case 2:
                    if (PupilMaintenance.mListener != null) {
                        PupilMaintenance.mListener.onPowerChanged(PupilDevice.currentDeviceStatus().getBattery(), this.onPower);
                    }
                    break;
                case 3:
                    ActionType action = ((ActionPacketContent) packet.getContent()).getAction();
                    switch (AnonymousClass2.$SwitchMap$com$kopin$accessory$packets$ActionType[action.ordinal()]) {
                        case 1:
                            this.onPower = true;
                            break;
                        case 2:
                            this.onPower = false;
                            break;
                    }
                    if (PupilMaintenance.mListener != null) {
                        PupilMaintenance.mListener.onPowerChanged(PupilDevice.currentDeviceStatus().getBattery(), this.onPower);
                    }
                    break;
            }
        }
    };

    public interface MaintenanceModeListener {
        void onChecksum(boolean z);

        void onDisconnect(boolean z);

        void onFlashComplete(int i);

        void onFlashErased();

        void onFlashError(FlashResponseContent.FlashErrorCodes flashErrorCodes);

        void onFlashProgress(int i);

        void onPowerChanged(int i, boolean z);

        void onReconnection(boolean z);

        void onVerified(boolean z);
    }

    static void init(PupilConnector headset) {
        headset.listenForPackets(PacketType.STATUS_RESPONSE, mFlashListener);
        headset.listenForPackets(PacketType.ACTION, mFlashListener);
        headset.listenForPackets(PacketType.FLASH_RESPONSE, mFlashListener);
    }

    static void onDisconnect(boolean wasLoss) {
        if (mListener != null) {
            mListener.onDisconnect(wasLoss);
        }
    }

    public static void setResponseListener(MaintenanceModeListener cb) {
        mListener = cb;
    }

    public static void checkCurrentMode() {
        PupilDevice.sendPacket(Flash.createBootBankQuery());
    }

    public static void rebootToApplicationMode() {
        if (mCurrentBootBank == 1) {
            mListener.onReconnection(false);
        } else if (mCurrentBootBank == 2) {
            PupilDevice.sendPacket(Flash.createSwitchBankAndReboot());
        } else {
            PupilDevice.sendPacket(Flash.createBootBankQuery());
        }
    }

    public static void rebootToMaintenanceMode() {
        if (mCurrentBootBank == 2) {
            mListener.onReconnection(true);
        } else if (mCurrentBootBank == 1) {
            PupilDevice.sendPacket(Flash.createSwitchBankAndReboot());
        } else {
            PupilDevice.sendPacket(Flash.createBootBankQuery());
        }
    }

    public static void clearFlash() {
        PupilDevice.sendPacket(Flash.createFlashErase());
    }

    public static void clearFlash(int start, int count) {
        PupilDevice.sendPacket(Flash.createFlashErase(start, count));
    }

    public static void flashBytes(int baseAddr, byte[] bytes, short checksum) {
        PupilDevice.sendPacket(Flash.createFlashProgram(baseAddr, bytes, checksum, mCurrentFlashOptions));
    }

    public static void checkSignature(int baseAddr, int count, byte[] sig) {
        PupilDevice.sendPacket(Flash.createSignatureVerify(baseAddr + 1048576, count, sig));
    }

    public static void getChecksum() {
    }

    public static void setOptionPreErase(boolean onOrOff) {
        if (onOrOff) {
            mCurrentFlashOptions = (byte) (mCurrentFlashOptions | FLASH_OPTS_PRE_ERASE);
        } else {
            mCurrentFlashOptions = (byte) (mCurrentFlashOptions & (FLASH_OPTS_PRE_ERASE ^ (-1)));
        }
    }

    public static void setProgressUpdateCount(int reportEveryKilobyte) {
        if (reportEveryKilobyte > FLASH_OPTS_PROGRESS_MAX) {
            reportEveryKilobyte = FLASH_OPTS_PROGRESS_MAX;
        }
        mCurrentFlashOptions = (byte) ((mCurrentFlashOptions & (FLASH_OPTS_PROGRESS_MASK ^ (-1))) | (reportEveryKilobyte << 1));
    }

    /* JADX INFO: renamed from: com.kopin.pupil.PupilMaintenance$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$kopin$accessory$base$PacketType = new int[PacketType.values().length];
        static final /* synthetic */ int[] $SwitchMap$com$kopin$accessory$packets$ActionType;
        static final /* synthetic */ int[] $SwitchMap$com$kopin$accessory$packets$FlashResponseContent$ResponseType;

        static {
            try {
                $SwitchMap$com$kopin$accessory$base$PacketType[PacketType.FLASH_RESPONSE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$kopin$accessory$base$PacketType[PacketType.STATUS_RESPONSE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$kopin$accessory$base$PacketType[PacketType.ACTION.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $SwitchMap$com$kopin$accessory$packets$ActionType = new int[ActionType.values().length];
            try {
                $SwitchMap$com$kopin$accessory$packets$ActionType[ActionType.POWER_CHARGING.ordinal()] = 1;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$kopin$accessory$packets$ActionType[ActionType.POWER_DISCHARGING.ordinal()] = 2;
            } catch (NoSuchFieldError e5) {
            }
            $SwitchMap$com$kopin$accessory$packets$FlashResponseContent$ResponseType = new int[FlashResponseContent.ResponseType.values().length];
            try {
                $SwitchMap$com$kopin$accessory$packets$FlashResponseContent$ResponseType[FlashResponseContent.ResponseType.BOOT_BANK.ordinal()] = 1;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$kopin$accessory$packets$FlashResponseContent$ResponseType[FlashResponseContent.ResponseType.PROGRAM_PROGRESS.ordinal()] = 2;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$kopin$accessory$packets$FlashResponseContent$ResponseType[FlashResponseContent.ResponseType.PROGRAM_COMPLETE.ordinal()] = 3;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$kopin$accessory$packets$FlashResponseContent$ResponseType[FlashResponseContent.ResponseType.ERASE_PROGRESS.ordinal()] = 4;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$kopin$accessory$packets$FlashResponseContent$ResponseType[FlashResponseContent.ResponseType.ERASE_COMPLETE.ordinal()] = 5;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$com$kopin$accessory$packets$FlashResponseContent$ResponseType[FlashResponseContent.ResponseType.READ_PROGRESS.ordinal()] = 6;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$com$kopin$accessory$packets$FlashResponseContent$ResponseType[FlashResponseContent.ResponseType.READ_COMPLETE.ordinal()] = 7;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$com$kopin$accessory$packets$FlashResponseContent$ResponseType[FlashResponseContent.ResponseType.VERIFY_RESULT.ordinal()] = 8;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$kopin$accessory$packets$FlashResponseContent$ResponseType[FlashResponseContent.ResponseType.ERROR.ordinal()] = 9;
            } catch (NoSuchFieldError e14) {
            }
        }
    }
}
