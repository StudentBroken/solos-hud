package com.kopin.accessory.utility;

import com.kopin.accessory.base.Packet;
import com.kopin.accessory.base.PacketContent;
import com.kopin.accessory.base.PacketHeader;
import com.kopin.accessory.base.PacketType;
import com.kopin.accessory.packets.FlashDataContent;
import com.kopin.accessory.packets.FlashEraseContent;
import com.kopin.accessory.packets.FlashVerifyContent;
import com.kopin.accessory.packets.headers.FlashBaseHeader;
import com.kopin.accessory.packets.headers.FlashProgramHeader;

/* JADX INFO: loaded from: classes14.dex */
public class Flash {
    public static Packet createBootBankQuery() {
        return new Packet(new PacketHeader(PacketType.QUERY_BOOT_BANK, 0), PacketContent.noContent());
    }

    public static Packet createSwitchBankAndReboot() {
        return new Packet(new PacketHeader(PacketType.SWITCH_BOOT_BANK, 0), PacketContent.noContent());
    }

    public static Packet createFlashErase() {
        FlashEraseContent data = FlashEraseContent.eraseApplicationBank();
        return new Packet(new PacketHeader(PacketType.FLASH_ERASE, data.getSize()), data);
    }

    public static Packet createFlashErase(int start, int count) {
        FlashEraseContent data = FlashEraseContent.eraseSectors(start, count);
        return new Packet(new PacketHeader(PacketType.FLASH_ERASE, data.getSize()), data);
    }

    public static Packet createFlashProgram(int start, byte[] bytes, short checksum, byte options) {
        FlashProgramHeader hdr = new FlashProgramHeader(start, bytes.length, checksum, options);
        FlashDataContent data = new FlashDataContent(hdr, bytes);
        return new Packet(new PacketHeader(PacketType.FLASH_PROGRAM, data.getSize()), data);
    }

    public static Packet createSignatureVerify(int start, int numBytes, byte[] signature) {
        FlashBaseHeader hdr = new FlashBaseHeader(start, numBytes);
        FlashVerifyContent data = new FlashVerifyContent(hdr, signature);
        return new Packet(new PacketHeader(PacketType.FLASH_VERIFY, data.getSize()), data);
    }
}
