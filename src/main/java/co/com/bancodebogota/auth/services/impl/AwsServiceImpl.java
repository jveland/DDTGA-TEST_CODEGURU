package co.com.bancodebogota.auth.services.impl;

import co.com.bancodebogota.auth.services.IAwsService;
import co.com.bancodebogota.aws.AWSParams;
import co.com.bancodebogota.aws.Enviroment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AwsServiceImpl implements IAwsService {

  @Value("${adapter.aws.accountId}")
  public String accountId;

  @Value("${adapter.aws.region}")
  public String region;

  @Value("${adapter.aws.cognitoRole}")
  public String cognitoRole;

  @Value("${adapter.aws.poolId}")
  public String poolId;

  @Value("${adapter.aws.sessionName}")
  public String sessionName;

  @Value("${adapter.aws.groupName}")
  public String groupName;

  public void init() {
    AWSParams params=new AWSParams();
    params.setAccountId(accountId);
    params.setRegion(region);
    params.setCognitoRole(cognitoRole);
    params.setPoolId(poolId);
    params.setSessionName(sessionName);
    params.setGroupName(groupName);
    Enviroment.instance.setParams(params);
  }
}
