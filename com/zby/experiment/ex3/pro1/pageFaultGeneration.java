package com.zby.experiment.ex3.pro1;

import java.util.HashMap;
import java.util.Map;

public class pageFaultGeneration {
    private static final int BLOCK_SIZE = 128; // 每块的字节数
    private Map<Integer, PageTableEntry> pageTable = new HashMap<>();

    public pageFaultGeneration() {
        pageTable.put(0, new PageTableEntry(0, 1, 5, 11));
        pageTable.put(1, new PageTableEntry(1, 1, 8, 12));
        pageTable.put(2, new PageTableEntry(2, 1, 9, 13));
        pageTable.put(3, new PageTableEntry(3, 1, 1, 21));
        pageTable.put(4, new PageTableEntry(4, 0, -1, 22));
        pageTable.put(5, new PageTableEntry(5, 0, -1, 23));
        pageTable.put(6, new PageTableEntry(6, 0, -1, 121));
    }

    // 地址转换操作
    public void translateAddress(int pageNumber, int unitOffset) {
        PageTableEntry entry = pageTable.get(pageNumber);

        if (entry == null) {
            System.out.println("页号不存在");
            return;
        }

        if (entry.flag == 1) {
            // int absoluteAddress = entry.blockNumber * BLOCK_SIZE + unitOffset;
            // 可以简化为:
            int absoluteAddress = (entry.blockNumber << 7) | unitOffset;
            System.out.println("绝对地址为: " + absoluteAddress);
        } else {
            // 未加载到主存，发生缺页中断
            System.out.println("** 缺页中断, 页号:" + pageNumber);
        }
    }

    public static void main(String[] args) {
        pageFaultGeneration pageFaultGeneration = new pageFaultGeneration();
        int[][] instructions = {
                {0, 70},
                {1, 50},
                {2, 15},
                {3, 21},
                {0, 56},
                {6, 40},
                {4, 53},
                {5, 23},
                {1, 37},
                {2, 78},
                {4, 1},
                {6, 84}
        };
        for (int[] instruction : instructions){
            int pageNumber = instruction[0];
            int unitOffset = instruction[1];
            System.out.println("访问页号：" + pageNumber + "，单元偏移：" + unitOffset);
            pageFaultGeneration.translateAddress(pageNumber, unitOffset);
            System.out.println();
        }
    }
}

class PageTableEntry {
    int pageNumber;      // 页号
    int flag;            // 标志位，1表示已加载到主存，0表示尚未加载
    int blockNumber;     // 主存块号
    int diskLocation;    // 磁盘上的位置

    public PageTableEntry(int pageNumber, int flag, int blockNumber, int diskLocation) {
        this.pageNumber = pageNumber;
        this.flag = flag;
        this.blockNumber = blockNumber;
        this.diskLocation = diskLocation;
    }
}


