/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package classes;

/**
 *
 * @author adminccs
 */
public class Queue<T> {

    private Node<T> front;
    private Node<T> rear;
    private int length;

    public Queue() {
        this.front = null;
        this.rear = null;
        this.length = 0;
    }

    public boolean isEmpty() {
        return front == null;
    }

    public void enqueue(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        } else {
            rear.setNext(newNode);
            rear = newNode;
        }
    }

    public T dequeue() {
        if (isEmpty()) {
            return null;
        }
        T data = front.getData();
        front = front.getNext();
        if (front == null) {
            rear = null;
        }
        return data;
    }

      public void remove(String data) {
        Node<T> current = front;
        Node<T> previous = null;
        
          
        while (current != null) {
            if ((current.getData().toString()).equals(data)) {
                System.out.println("AUMENTO DE PRIORIDAD PARA "+ data);
                if (previous == null) {
                    front = current.getNext();
                    if (front == null) {
                        rear = null;
                    }
                } else {
                    previous.setNext(current.getNext());
                    if (current.getNext() == null) {
                        rear = previous;
                    }
                }
                length--;
                return; // Elemento encontrado y eliminado
            }

            previous = current;
            current = current.getNext();
        }
    }
    public T peek() {
        return isEmpty() ? null : front.getData();
    }

    public Node<T> getFront() {
        return front;
    }

    public void setFront(Node<T> front) {
        this.front = front;
    }

    public Node<T> getRear() {
        return rear;
    }

    public void setRear(Node<T> rear) {
        this.rear = rear;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

}
