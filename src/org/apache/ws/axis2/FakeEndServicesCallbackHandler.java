
/**
 * FakeEndServicesCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package org.apache.ws.axis2;

    /**
     *  FakeEndServicesCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class FakeEndServicesCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public FakeEndServicesCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public FakeEndServicesCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for removeFile method
            * override this method for handling normal response from removeFile operation
            */
           public void receiveResultremoveFile(
                    org.apache.ws.axis2.FakeEndServicesStub.RemoveFileResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from removeFile operation
           */
            public void receiveErrorremoveFile(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for runFile method
            * override this method for handling normal response from runFile operation
            */
           public void receiveResultrunFile(
                    org.apache.ws.axis2.FakeEndServicesStub.RunFileResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from runFile operation
           */
            public void receiveErrorrunFile(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deploycallBack method
            * override this method for handling normal response from deploycallBack operation
            */
           public void receiveResultdeploycallBack(
                    org.apache.ws.axis2.FakeEndServicesStub.DeploycallBackResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deploycallBack operation
           */
            public void receiveErrordeploycallBack(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deployPlatform method
            * override this method for handling normal response from deployPlatform operation
            */
           public void receiveResultdeployPlatform(
                    org.apache.ws.axis2.FakeEndServicesStub.DeployPlatformResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deployPlatform operation
           */
            public void receiveErrordeployPlatform(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for renameFile method
            * override this method for handling normal response from renameFile operation
            */
           public void receiveResultrenameFile(
                    org.apache.ws.axis2.FakeEndServicesStub.RenameFileResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from renameFile operation
           */
            public void receiveErrorrenameFile(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for blablabla method
            * override this method for handling normal response from blablabla operation
            */
           public void receiveResultblablabla(
                    org.apache.ws.axis2.FakeEndServicesStub.BlablablaResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from blablabla operation
           */
            public void receiveErrorblablabla(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for addFile method
            * override this method for handling normal response from addFile operation
            */
           public void receiveResultaddFile(
                    org.apache.ws.axis2.FakeEndServicesStub.AddFileResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from addFile operation
           */
            public void receiveErroraddFile(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for setRunnable method
            * override this method for handling normal response from setRunnable operation
            */
           public void receiveResultsetRunnable(
                    org.apache.ws.axis2.FakeEndServicesStub.SetRunnableResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from setRunnable operation
           */
            public void receiveErrorsetRunnable(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getFile method
            * override this method for handling normal response from getFile operation
            */
           public void receiveResultgetFile(
                    org.apache.ws.axis2.FakeEndServicesStub.GetFileResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getFile operation
           */
            public void receiveErrorgetFile(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getStatus method
            * override this method for handling normal response from getStatus operation
            */
           public void receiveResultgetStatus(
                    org.apache.ws.axis2.FakeEndServicesStub.GetStatusResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getStatus operation
           */
            public void receiveErrorgetStatus(java.lang.Exception e) {
            }
                


    }
    