package com.zby.experiment.ex2.pro1;

class PCB {
    String name;
    PCB next;
    int needTime;
    int priority;
    String state;
    int CPUTime;
    int RoundTime;    // finishTime - arrivalTime
    int WaitingTime;  // finishTime - arrivalTime - needTime

    public PCB(String name, int needTime, int priority) {
        this.name = name;
        this.needTime = needTime;
        this.priority = priority;
        this.state = "ready";
        this.next = null;
        this.CPUTime = 0;
    }
}

class Scheduler {
    PCB head;

    public void insertProcess(PCB process) {
        if (head == null) {
            head = process;
            return;
        }

        PCB current = head;
        PCB previous = null;

        while (current != null && current.priority >= process.priority) {
            previous = current;
            current = current.next;
        }

        if (previous == null) {
            process.next = head;
            head = process;
        } else {
            previous.next = process;
            process.next = current;
        }
    }

    public PCB getNextProcess() {
        if (head == null) return null;
        PCB process = head;
        head = head.next;
        process.next = null; // 断开链表
        return process;
    }

    public static void printQueue(PCB current) {
        StringBuilder sb = new StringBuilder();
        while (current != null) {
            sb.append(current.name)
                    .append("(priority:").append(current.priority)
                    .append(", time:").append(current.needTime)
                    .append(", state:").append(current.state).append(") -> ");
            current = current.next;
        }
        sb.append("null");
        System.out.println(sb.toString());
    }
}

public class PriorityScheduling {
    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler();

        // 示例输入：进程名、要求运行时间、优先级
        PCB[] processes = {
                new PCB("P1", 2, 1),
                new PCB("P2", 3, 5),
                new PCB("P3", 1, 3),
                new PCB("P4", 2, 4),
                new PCB("P5", 4, 2)
        };

        int need_time_cpy[] = new int[processes.length];
        for (int i = 0; i < processes.length; i++) {
            need_time_cpy[i] = processes[i].needTime;
        }

        // 按初始优先级构建队列
        for (PCB p : processes) {
            scheduler.insertProcess(p);
        }

        System.out.println("INPUTNAME\tNEEDTIME AND PRIORITY" );
        for (PCB p : processes) {
            System.out.println(p.name + "\t\t\t" + p.needTime + "\t\t\t\t" + p.priority);
        }
        System.out.println("OUTPUT OF PRIORITY");

        System.out.println("NAME\t\tCPUTIME\t\tNEEDTIME\t\tPRIORITY\t\tSTATE");
        int CPUTime = 0;
        System.out.println("CPUTIME:" + CPUTime);
        for (PCB p : processes) {
            System.out.println(p.name + "\t\t\t" +p.CPUTime +"\t\t\t" + p.needTime + "\t\t\t\t" + p.priority + "\t\t\t\t" + p.state);
        }

        int finished = 0;
        while (finished < 5) {
            PCB current = scheduler.getNextProcess();
            if (current == null) break;

            CPUTime++;
            System.out.println("CPUTIME:" + CPUTime);
            System.out.println("NAME\t\tCPUTIME\t\tNEEDTIME\t\tPRIORITY\t\tSTATE");

            current.priority--;
            current.needTime--;

            if (current.needTime== 0) {
                current.state = "finish";
                finished++;
                current.RoundTime = CPUTime;
                int num =Integer.parseInt(current.name.substring(1));
                current.WaitingTime = CPUTime - need_time_cpy[num - 1];
                // System.out.println("进程 " + current.name + " 结束，状态: " + current.state);
            } else {
                current.state = "working";
                scheduler.insertProcess(current); // 重新插入队列
                // System.out.println("进程 " + current.name + " 重新入队，状态: " + current.state);
            }

            // 维护PCB.CPUTime
            for (PCB p : processes) {
                if (p.equals(current) || (p.CPUTime != 0 && !p.state.equals("finish"))) {
                    p.CPUTime++;
                }
            }

            for (PCB p : processes) {
                System.out.println(p.name + "\t\t\t" +p.CPUTime +"\t\t\t" + p.needTime + "\t\t\t\t" + p.priority + "\t\t\t\t" + p.state);
            }

            if (current.needTime != 0) {
                current.state = "ready";
            }

            // System.out.print("当前队列状态: ");
            // Scheduler.printQueue(scheduler.head);
        }

        System.out.println("\n所有进程已结束。");
        System.out.println("NAME\tRoundTime\tWaitingTime");
        for (PCB p : processes) {
            System.out.println(p.name + "\t\t" + p.RoundTime + "\t\t\t" + p.WaitingTime);
        }
    }
}