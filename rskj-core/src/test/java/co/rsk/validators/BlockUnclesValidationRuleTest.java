package co.rsk.validators;

import co.rsk.blockchain.utils.BlockGenerator;
import co.rsk.core.bc.BlockChainImpl;
import co.rsk.core.bc.BlockChainImplTest;
import com.fasterxml.jackson.databind.node.BigIntegerNode;
import org.ethereum.core.Block;
import org.ethereum.core.BlockHeader;
import org.ethereum.db.BlockStore;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajlopez on 18/08/2017.
 */
public class BlockUnclesValidationRuleTest {
    @Test
    public void rejectBlockWithSiblingUncle() {
        Block genesis = BlockGenerator.getGenesisBlock();

        Block block1 = BlockGenerator.createChildBlock(genesis);
        Block uncle = BlockGenerator.createChildBlock(block1);
        List<BlockHeader> uncles = new ArrayList<>();
        uncles.add(uncle.getHeader());

        Block block = BlockGenerator.createChildBlock(block1, null, uncles, 1, null);

        BlockChainImpl blockChain = BlockChainImplTest.createBlockChain();
        BlockStore store = blockChain.getBlockStore();

        store.saveBlock(genesis, BigInteger.valueOf(1), true);
        store.saveBlock(block1, BigInteger.valueOf(2), true);

        BlockUnclesValidationRule rule = new BlockUnclesValidationRule(store, 10, 10, new BlockCompositeRule(), new BlockParentCompositeRule());

        Assert.assertFalse(rule.isValid(block));
    }

    @Test
    public void rejectBlockWithUncleHavingHigherNumber() {
        Block genesis = BlockGenerator.getGenesisBlock();

        Block block1 = BlockGenerator.createChildBlock(genesis);
        Block uncle1 = BlockGenerator.createChildBlock(block1);
        Block uncle2 = BlockGenerator.createChildBlock(uncle1);
        List<BlockHeader> uncles = new ArrayList<>();
        uncles.add(uncle2.getHeader());

        Block block = BlockGenerator.createChildBlock(block1, null, uncles, 1, null);

        BlockChainImpl blockChain = BlockChainImplTest.createBlockChain();
        BlockStore store = blockChain.getBlockStore();

        store.saveBlock(genesis, BigInteger.valueOf(1), true);
        store.saveBlock(block1, BigInteger.valueOf(2), true);

        BlockUnclesValidationRule rule = new BlockUnclesValidationRule(store, 10, 10, new BlockCompositeRule(), new BlockParentCompositeRule());

        Assert.assertFalse(rule.isValid(block));
    }
}
