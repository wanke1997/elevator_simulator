import java.util.HashMap;
import java.util.Map;

// 系统参数
class SysParam {
    public Map<String,String> configs;
    
    public SysParam(String[] keys, String[] values) {
        configs = new HashMap<>();
        for(int i=0;i<keys.length;i++) {
            configs.put(keys[i],values[i]);
        }
    }
}

// 用户指令数据
class UserCall {
    public int userFloor;
    public int userTarget;
    public int callTime;
    public char callType;

    public UserCall(int userFloor, int userTarget, int callTime, char callType) {
        this.userFloor = userFloor;
        this.userTarget = userTarget;
        this.callTime = callTime;
        this.callType = callType;
    }
}

// 电梯服务指令队列（双向链表）
class ServeListNode {
    public char serveState;
    public UserCall userCall;
    public ServeListNode prev;
    public ServeListNode next;

    public ServeListNode(char serveState, UserCall userCall) {
        this.serveState = serveState;
        this.userCall = userCall;
        prev = null;
        next = null;
    }
}

// 电梯状态
class ElevatorState {
    public int currentFloor;
    public char runState;
    // 电梯服务队列
    public ServeListNode serveList;

    public ElevatorState(int currentFloor, char runState) {
        this.currentFloor = currentFloor;
        this.runState = runState;
        serveList = null;
    }
}

// 电梯声明
// ElevatorState elevatorA;
// ElevatorState elevatorB;

// 电梯待响应数据
class ResponseListNode {
    public UserCall userCall;
    public ResponseListNode prev;
    public ResponseListNode next;

    public ResponseListNode(UserCall userCall) {
        this.userCall = userCall;
        prev = null;
        next = null;
    }
}

class ResponseListHeadNode {
    public ResponseListNode head;
    public ResponseListNode tail;

    public ResponseListHeadNode() {
        head = new ResponseListNode(null);
        tail = new ResponseListNode(null);
        head.next = tail;
        tail.prev = head;
    }
}

// 电梯待响应指令队列（双向链表）
// ResponseListHeadNode responseList;