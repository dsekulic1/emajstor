import React from 'react'
import { VictoryChart } from 'victory'
import { VictoryBar } from 'victory'

export default function Chart() {
  const data = [
    { numstars: 1, count: 100 },
    { numstars: 2, count: 200 },
    { numstars: 3, count: 111 },
    { numstars: 4, count: 333 },
    { numstars: 5, count: 123 },
  ]
  return (
    <g>
      <h3 style={{ marginTop: '5px' }}>Review Statistics</h3>

      <VictoryChart domainPadding={20}>
        <VictoryBar
          data={data}
          // data accessor for x values
          x='numstars'
          // data accessor for y values
          y='count'
        />
      </VictoryChart>
    </g>
  )
}
