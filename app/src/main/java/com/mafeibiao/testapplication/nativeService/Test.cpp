int main()
{
    sp<ProcessState>proc(ProcessState:self());
    sp<IServiceManager>sm=defaultServiceManager();
    //记住注册你的服务,否则谁也找不着你！
    sm->addService("service.name",new Test());
    //如果压力不大,可以不用单独搞一个线程。
    ProcessState:self()->startThreadPool();
    //这个是必须的,否则主线程退出了,你也完了。
    IPCThreadState:self()->joinThreadPool();
}