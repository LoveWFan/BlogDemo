class ITest:public IInterface
{
    public:
    //神奇的宏DECLARE_META_INTERFACE。
    DECLARE_META_INTERFACE(Test);
    virtual void getTest()=0;
    virtual void setTest()=0;
}//ITest是一个接口类

class BnTest:public BnInterface<ITest>
{
    public:
    //由于ITest是个纯虚类,而BnTest只实现了onTransact函数,所以BnTest依然是一个纯虚类。
    virtual status_t onTransact(
        uint32_t code,
        const Parcel&data,
        Parcel*reply,
        uint32_t flags=0
    );
};