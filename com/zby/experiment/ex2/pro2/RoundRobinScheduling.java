package com.zby.experiment.ex2.pro2;

class PCB {
    String name;
    PCB next;
    int needTime;
    int elapsedTime;
    String state;

    public PCB(String name, int needTime) {
        this.name = name;
        this.needTime = needTime;
        this.elapsedTime = 0;
        this.state = "R";
        this.next = null;
    }
}

public class RoundRobinScheduling {

    // 查找当前进程的前驱节点
    private static PCB findPrevious(PCB current) {
        if (current == null) return null;
        PCB prev = current;
        while (prev.next != current) {
            prev = prev.next;
        }
        return prev;
    }

    // 打印所有进程信息
    private static void printProcesses(PCB[] processes, int cpuTime) {
        System.out.println("CPUTIME: " + cpuTime);
        System.out.println("进程名\t指针\t\t要求运行时间\t已运行时间\t状态");
        for (PCB p : processes) {
            String nextName = (p.next != null) ? p.next.name : "null";
            System.out.printf("%s\t\t%s\t\t%d\t\t\t%d\t\t\t%s%n",
                    p.name, nextName, p.needTime, p.elapsedTime, p.state);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        // 初始化进程（示例数据，要求运行时间可调整）
        PCB Q1 = new PCB("Q1", 2);
        PCB Q2 = new PCB("Q2", 3);
        PCB Q3 = new PCB("Q3", 1);
        PCB Q4 = new PCB("Q4", 2);
        PCB Q5 = new PCB("Q5", 4);

        // 构建循环队列
        Q1.next = Q2;
        Q2.next = Q3;
        Q3.next = Q4;
        Q4.next = Q5;
        Q5.next = Q1;

        PCB current = Q1; // 当前运行的进程
        PCB[] processes = {Q1, Q2, Q3, Q4, Q5};
        int finished = 0;
        int cpuTime = 0;

        // 初始状态输出
        printProcesses(processes, 0);

        while (finished < 5) {
            if (current.state.equals("E")) {
                current = current.next;
                continue;
            }

            // 运行当前进程一个时间片
            current.elapsedTime++;
            cpuTime++;

            // 判断是否完成
            if (current.elapsedTime == current.needTime) {
                current.state = "E";
                finished++;

                // 调整前驱节点的指针
                PCB prev = findPrevious(current);
                if (prev != null) {
                    prev.next = current.next;
                }
                System.out.println("进程 " + current.name + " 完成，移出队列。");
            }

            // 打印当前状态
            printProcesses(processes, cpuTime);

            // 移动到下一个就绪进程
            current = current.next;
        }

        System.out.println("所有进程已结束。");
    }
}