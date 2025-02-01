package francisco.personal.blockchain.entities;

import francisco.personal.blockchain.Utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class MerkleTree {

    public static String getRoot(List<String> transactionsIds){
        if(transactionsIds.isEmpty()){
            return StringUtil.applySha256("");
        }

        List<String> treeLayer = transactionsIds;

        while (treeLayer.size() > 1){
            treeLayer = computeNextLayer(treeLayer);
        }

        return treeLayer.get(0);
    }

    private static List<String> computeNextLayer(List<String> treeLayer){
        List<String> nextLayer = new ArrayList<>();

        for(int i = 0; i < treeLayer.size(); i+=2){
            String left = treeLayer.get(i);
            String right = (i + 1 < treeLayer.size()) ? treeLayer.get(i + 1) : left;
            nextLayer.add(StringUtil.applySha256(left + right));
        }

        return nextLayer;
    }
}
