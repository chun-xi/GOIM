package ec.mark.im.common.vo;

public class ChatMsg {
      private String msgtype;//消息类型


        private String username;  //消息来源用户名
        private String avatar;  //消息来源用户头像
        private String id;  //消息来源id  --用户或者群组id
        private String type;  //类型
        private String content;//消息
    //新增
       private String sign ;//签名
        private  String groupid;//分组id
        private String groupname;


	public String getGroupname() {
			return groupname;
		}

		public void setGroupname(String groupname) {
			this.groupname = groupname;
		}

	public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    /**
         * 消息idi
         */
        private  int cid=0;   //消息id，可不传。除非你要对消息进行一些操作（如撤回）
        /**
         *
         */
        private boolean mine=false; // false //是否我发送的消息，如果为true，则会显示在右方

        /**
         * 来源用户
         */
        private  String fromid;
        /**
         * 时间戳
         */
        private  long timestamp;//时间戳

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

        public boolean isMine() {
            return mine;
        }

        public void setMine(boolean mine) {
            this.mine = mine;
        }

        public String getFromid() {
            return fromid;
        }

        public void setFromid(String fromid) {
            this.fromid = fromid;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }


}
