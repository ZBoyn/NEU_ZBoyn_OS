package com.zby.experiment.ex3.pro2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PageFaultHandlerFIFO {
    private static final int BLOCK_SIZE = 128;
    private static final int MEMORY_SIZE = 3; // 主存页框数
    private Map<Integer, PageTableEntry> pageTable = new HashMap<>();
    private int[] memory = new int[MEMORY_SIZE]; // 主存页框数组
    private int pointer = 0; // FIFO指针

    public PageFaultHandlerFIFO() {
        pageTable.put(0, new PageTableEntry(0, 1, 5, 0, 11));
        pageTable.put(1, new PageTableEntry(1, 1, 8, 0, 12));
        pageTable.put(2, new PageTableEntry(2, 1, 9, 0, 13));
        pageTable.put(3, new PageTableEntry(3, 1, 1, 0, 21));
        pageTable.put(4, new PageTableEntry(4, 0, -1, 0, 22));
        pageTable.put(5, new PageTableEntry(5, 0, -1, 0, 23));
        pageTable.put(6, new PageTableEntry(6, 0, -1, 0, 121));
        // 初始化主存页框
        Arrays.fill(memory, -1);
    }

    public void translateAddress(int pageNumber, int unitOffset){
        PageTableEntry entry = pageTable.get(pageNumber);

        if (entry == null){
            System.out.println("页号不存在");
            return;
        }

        if (entry.flag == 1){
            int absoluteAddress = (entry.blockNumber << 7) | unitOffset;
            System.out.println("绝对地址为: " + absoluteAddress);
        } else {
            handlePageFaultWithFIFO(pageNumber);
        }

    }

    private void handlePageFaultWithFIFO(int pageNumber){
        System.out.println("** 缺页中断, 页号: " + pageNumber);

        // 检查页面是否缓存到主存中
        for (int i = 0; i < MEMORY_SIZE; i++){
            if (memory[i] == pageNumber){
                System.out.println("页号 " + pageNumber + " 已在主存中, 无需替换");
                return;
            }
        }

        int replacedPage = memory[pointer];
        if (replacedPage != -1) {
            PageTableEntry replacedEntry = pageTable.get(replacedPage);
            // 如果被替换页已修改，写回磁盘
            if (replacedEntry.modified == 1) {
                System.out.println("页 " + replacedPage + " 已修改，写回磁盘");
            }
            replacedEntry.flag = 0; // 更新页表标志位
            replacedEntry.blockNumber = -1;
        }

        // 将新页加载到主存
        PageTableEntry newPage = pageTable.get(pageNumber);
        newPage.flag = 1;
        newPage.blockNumber = pointer; // 使用指针记录主存块号
        memory[pointer] = pageNumber;

        System.out.println("将页 " + pageNumber + " 加载到主存块 " + pointer);

        // 更新FIFO指针
        pointer = (pointer + 1) % MEMORY_SIZE;


    }
    public void displayMemoryState() {
        System.out.println("主存状态:");
        for (int i = 0; i < MEMORY_SIZE; i++) {
            System.out.println("块 " + i + ": " + (memory[i] == -1 ? "空" : "页号 " + memory[i]));
        }
    }

    public static void main(String[] args) {
        PageFaultHandlerFIFO handler = new PageFaultHandlerFIFO();
        int[][] instructions = {
                {0, 70}, {1, 50}, {2, 15}, {3, 21}, {0, 56}, {6, 40},
                {4, 53}, {5, 23}, {1, 37}, {2, 78}, {4, 1}, {6, 84}
        };

        for (int[] instruction : instructions) {
            int pageNumber = instruction[0];
            int unitOffset = instruction[1];
            System.out.println("访问页号：" + pageNumber + ", 单元偏移：" + unitOffset);
            handler.translateAddress(pageNumber, unitOffset);
            handler.displayMemoryState();
            System.out.println();
        }
    }
}

class PageTableEntry {
    int pageNumber;      // 页号
    int flag;            // 标志位, 1表示已加载到主存, 0表示尚未加载
    int blockNumber;     // 主存块号
    int modified;        // 修改标志, 1表示已经修改过, 0表示未修改
    int diskLocation;    // 磁盘上的位置

    public PageTableEntry(int pageNumber, int flag, int blockNumber, int modified, int diskLocation) {
        this.pageNumber = pageNumber;
        this.flag = flag;
        this.blockNumber = blockNumber;
        this.modified = modified;
        this.diskLocation = diskLocation;
    }
}
