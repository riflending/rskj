/*
 * This file is part of RskJ
 * Copyright (C) 2020 RSK Labs Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package co.rsk.net.light.message;

import org.bouncycastle.util.BigIntegers;
import org.ethereum.util.RLP;
import org.ethereum.util.RLPList;

import java.math.BigInteger;

import static co.rsk.net.light.LightClientMessageCodes.GET_BLOCK_RECEIPTS;


public class GetBlockReceiptsMessage extends LightClientMessage {
    private final long id;
    private final byte[] hash;

    public GetBlockReceiptsMessage(long id, byte[] hash) {
        this.id = id;
        this.hash = hash.clone();
        this.code = GET_BLOCK_RECEIPTS.asByte();
    }

    public GetBlockReceiptsMessage(byte[] encoded) {
        RLPList list = (RLPList) RLP.decode2(encoded).get(0);
        byte[] rlpId = list.get(0).getRLPData();
        this.id = rlpId == null ? 0 : BigIntegers.fromUnsignedByteArray(rlpId).longValue();
        this.hash = list.get(1).getRLPData();
        this.code = GET_BLOCK_RECEIPTS.asByte();
    }

    public long getId() {
        return id;
    }

    public byte[] getBlockHash() {
        return this.hash.clone();
    }

    @Override
    public byte[] getEncoded() {
        byte[] rlpId = RLP.encodeBigInteger(BigInteger.valueOf(getId()));
        byte[] rlpHash = RLP.encodeElement(getBlockHash());
        return RLP.encodeList(rlpId, rlpHash);
    }

    @Override
    public Class<?> getAnswerMessage() {
        return BlockReceiptsMessage.class;
    }

    @Override
    public String toString() {
        return "";
    }
}