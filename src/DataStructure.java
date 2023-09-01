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
class ServeListHeadNode {
    public ServeListNode head;
    public ServeListNode tail;
    public int nodeNum;

    public ServeListHeadNode() {
        head = new ServeListNode('N', null, -1);
        tail = new ServeListNode('N', null, -1);
        head.next = tail;
        tail.prev = head;
        nodeNum = 0;
    }
}

class ServeListNode {
    // 'E' start serving, or 'P' before serving
    public char serveState;
    public UserCall userCall;
    public int userCallIdx;
    public ServeListNode prev;
    public ServeListNode next;

    public ServeListNode(char serveState, UserCall userCall, int userCallIdx) {
        this.serveState = serveState;
        this.userCall = userCall;
        this.userCallIdx = userCallIdx;
        prev = null;
        next = null;
    }
}

// 电梯状态
class ElevatorState {
    public int currentFloor;
    // 'S', 'U', or 'D'
    public char runState;
    // 电梯服务队列
    public ServeListHeadNode serveList;

    public ElevatorState(int currentFloor, char runState) {
        this.currentFloor = currentFloor;
        this.runState = runState;
        serveList = new ServeListHeadNode();
    }
}

// 电梯待响应数据
class ResponseListNode {
    public int userCallIdx;
    public ResponseListNode prev;
    public ResponseListNode next;

    public ResponseListNode(int userCallIdx) {
        this.userCallIdx = userCallIdx;
        prev = null;
        next = null;
    }
}

class ResponseListHeadNode {
    public ResponseListNode head;
    public ResponseListNode tail;
    public int nodeNum;
    public int nextIdx;

    public ResponseListHeadNode() {
        head = new ResponseListNode(-1);
        tail = new ResponseListNode(-1);
        head.next = tail;
        tail.prev = head;
        nodeNum = 0;
        nextIdx = 0;
    }
}
