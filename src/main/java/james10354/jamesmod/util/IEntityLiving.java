package james10354.jamesmod.util;

public interface IEntityLiving {
    float getMoveSpeed();

    void setMoveSpeed(float moveSpeed);

    float getMoveSpeedMultiplier();
    void setMoveSpeedMultiplier(float multiplier);

    boolean isSprinting();
    void setSprinting(boolean isSprinting);

    float getSprintBoost();
    void setSprintBoost(float sprintBoost);
}
