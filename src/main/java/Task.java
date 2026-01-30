public abstract class Task {
    private String description;
    private boolean status;

    public Task(String description){
        this.description = description;
        this.status = false;
    }

    public void mark(){
        status = true;
    }

    public void unmark(){
        status = false;
    }

    public String getStatus(){
        if(status) {
            return "X";
        }else{
            return " ";
        }
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return status;
    }

    public abstract String getType();

    @Override
    public String toString() {
        return "[" + getType() + "][" + getStatus() + "] " + description;
    }
}
