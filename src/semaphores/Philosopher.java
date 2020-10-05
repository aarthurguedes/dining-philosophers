package semaphores;

import enums.PhilosopherState;

import java.util.concurrent.Semaphore;

public class Philosopher implements Runnable {
    private int id;
    private PhilosopherState state;
    private Semaphore mutex;
    private Table table;
    private final int ACTION_TIME = 1000;

    public Philosopher(int id, Table table) {
        this.id = id;
        this.mutex = new Semaphore(0);
        this.table = table;
        this.state = PhilosopherState.THINKING;
        new Thread((Runnable) this, String.format("Philosopher %d", this.id)).start();
    }

    public PhilosopherState getState() {
        return state;
    }

    public void setState(PhilosopherState state) {
        this.state = state;
    }

    public void block() {
        try {
            this.mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void release() {
        this.mutex.release();
    }

    public void think() {
        try {
            Thread.sleep(ACTION_TIME);
        } catch (InterruptedException ie) {
            System.out.println(String.format("O filosofo %d pensou demais", this.id));
        }
    }

    public void eat() {
        try {
            Thread.sleep(ACTION_TIME);
        } catch (InterruptedException ie) {
            System.out.println(String.format("O filosofo %d comeu demais", this.id));
        }
    }

    @Override
    public void run() {
        while (true) {
            this.think();
            this.table.getForks(this.id);
            this.eat();
            this.table.returnForks(this.id);
        }
    }
}
